package at.ac.htlinn.hamsterbackend.courseManagement.teacher;

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
import at.ac.htlinn.hamsterbackend.courseManagement.course.dto.CourseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.SolutionService;
import at.ac.htlinn.hamsterbackend.courseManagement.student.StudentService;
import at.ac.htlinn.hamsterbackend.courseManagement.teacher.dto.TeacherCourseDto;
import at.ac.htlinn.hamsterbackend.user.model.User;

@SpringBootTest
public class TeacherServiceTest {
	
	@Autowired
	TeacherService teacherService;
	
    @MockBean
    TeacherRepository teacherRepository;
    @MockBean
	StudentService studentService;
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
	private final Exercise exercise = Exercise.builder()
			.id(3)
			.name("HamsterSolution")
			.course(course)
			.build();

    @Test
    public void getTeacherViewTest() {
    	List<Course> courses = Arrays.asList(course);
    	when(courseService.getCoursesByTeacherId(user.getId())).thenReturn(courses);
    	List<CourseDto> found = teacherService.getTeacherView(user.getId());
    	
    	assertEquals(found.size(), 1);
    	assertEquals(found.get(0).getName(), course.getName());
    }

    @Test
    public void getCourseViewTest() {
    	List<User> students = Arrays.asList(user);
    	List<Activity> activities = Arrays.asList(exercise);
    	int submitted = 4;
    	int feedbacked = 3;
    	
    	when(courseService.getCourseById(course.getId())).thenReturn(course);
    	when(studentService.getAllStudentsInCourse(course.getId())).thenReturn(students);
    	when(activityService.getAllActivitiesInCourse(course.getId())).thenReturn(activities);
    	when(solutionService.getNumberOfSubmittedSolutions(exercise.getId())).thenReturn(submitted);
    	when(solutionService.getNumberOfFeedbackedSolutions(exercise.getId())).thenReturn(feedbacked);
    	
    	TeacherCourseDto found = teacherService.getCourseView(course.getId());

    	assertEquals(found.getActivityViews().size(), 1);
    	assertEquals(found.getActivityViews().get(0).getName(), exercise.getName());
    	assertEquals(found.getActivityViews().get(0).getSubmitted(), submitted);
    	assertEquals(found.getActivityViews().get(0).getFeedbacked(), feedbacked);
    }
}
