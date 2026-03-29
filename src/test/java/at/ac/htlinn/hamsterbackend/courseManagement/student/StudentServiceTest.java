package at.ac.htlinn.hamsterbackend.courseManagement.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.ActivityService;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.SolutionService;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.model.Solution;
import at.ac.htlinn.hamsterbackend.courseManagement.student.dto.StudentCourseDto;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import at.ac.htlinn.hamsterbackend.user.model.User;

@SpringBootTest
public class StudentServiceTest {
	
	@Autowired
	StudentService studentService;
	
    @MockBean
    StudentRepository studentRepository;
    @MockBean
	CourseService courseService;
    @MockBean
	ActivityService activityService;
    @MockBean
	SolutionService solutionService;
    
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
			.id(3)
			.name("HamsterSolution")
			.course(course)
			.terrain(terrainObject)
			.build();
	private final Solution solution = Solution.builder()
			.id(4)
			.activity(exercise)
			.student(user)
			.code("abc")
			.build();
	
    @Test
    public void getStudentViewTest() {
    	List<Course> courses = Arrays.asList(course);
    	List<Activity> activities = Arrays.asList(exercise);
    	
        when(courseService.getCoursesByStudentId(user.getId())).thenReturn(courses);
    	when(activityService.getAllActivitiesInCourse(course.getId())).thenReturn(activities);
    	when(solutionService.getSolutionByActivityAndStudentId(exercise.getId(), user.getId())).thenReturn(solution);
    	
    	List<StudentCourseDto> found = studentService.getStudentView(user.getId());
    	
    	assertEquals(found.size(), 1);
    	assertEquals(found.get(0).getCourseName(), course.getName());
    	assertEquals(found.get(0).getActivityViews().get(0).getName(), exercise.getName());
    	assertEquals(found.get(0).getActivityViews().get(0).getSolutionView().getCode(), solution.getCode());
    }
}
