package at.ac.htlinn.hamsterbackend.courseManagement.activity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObjectService;
import at.ac.htlinn.hamsterbackend.user.model.User;

@SpringBootTest
public class ActivityServiceTest {
	
	@Autowired
	ActivityService activityService;
	
    @MockBean
    ActivityRepository activityRepository;
    @MockBean
    CourseService courseService;
    @MockBean
    TerrainObjectService terrainObjectService;
    
	private final User user = User.builder()
			.id(1)
			.build();
	private final Course course = Course.builder()
			.id(2)
			.name("Hamster")
			.teacher(user)
			.build();
    private final TerrainObject terrainObject = TerrainObject.builder()
    		.terrainId(3)
            .build();
	private final Exercise exercise = Exercise.builder()
			.id(4)
			.name("HamsterExercise")
			.course(course)
			.terrain(terrainObject)
			.build();
    
    @Test
    public void getActivityByIdTest() {
    	when(activityRepository.getById(exercise.getId())).thenReturn(exercise);
    	Exercise found = (Exercise) activityService.getActivityById(exercise.getId());
    	assertEquals(found.getName(), exercise.getName());
    }
    
    @Test
    public void saveActivityTest() {
    	when(activityRepository.save(exercise)).thenReturn(exercise);
    	Exercise saved = (Exercise) activityService.saveActivity(exercise);
    	assertEquals(saved.getName(), exercise.getName());
    }
    
    @Test
    public void updateActivityTest() throws NoSuchFieldException, Exception {
    	Exercise updatedExercise = Exercise.builder()
    			.id(4)
    			.name("HamsterExerciseUpdated")
    			.course(course)
    			.terrain(terrainObject)
    			.build();
    	
    	HashMap<String, Object> fields = new HashMap<String, Object>();
    	fields.put("name", updatedExercise.getName());

    	when(courseService.getCourseById(course.getId())).thenReturn(course);
    	when(terrainObjectService.getTerrainObject(terrainObject.getTerrainId())).thenReturn(terrainObject);
    	when(activityRepository.save(updatedExercise)).thenReturn(updatedExercise);
		Exercise updated = (Exercise) activityService.updateActivity(exercise, fields);
    	assertEquals(updated.getName(), updatedExercise.getName());
    }
    
    @Test
    public void deleteActivityTest() {
    	boolean success = activityService.deleteActivity(exercise.getId());
    	assertTrue(success);
    }
    
    @Test
    public void getAllActivitiesInCourseTest() {
        List<Activity> activities = Arrays.asList(exercise);
        
        when(activityRepository.getByCourseId(course.getId())).thenReturn(activities);
        List<Activity> found = activityService.getAllActivitiesInCourse(course.getId());
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getName(), exercise.getName());
    }
}
