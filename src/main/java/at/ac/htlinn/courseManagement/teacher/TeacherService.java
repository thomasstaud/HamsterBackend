package at.ac.htlinn.courseManagement.teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.activity.ActivityService;
import at.ac.htlinn.courseManagement.activity.model.Activity;
import at.ac.htlinn.courseManagement.course.CourseService;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.course.model.CourseDto;
import at.ac.htlinn.courseManagement.solution.SolutionService;
import at.ac.htlinn.courseManagement.student.StudentService;
import at.ac.htlinn.courseManagement.teacher.model.TeacherActivityDto;
import at.ac.htlinn.courseManagement.teacher.model.TeacherCourseDto;
import at.ac.htlinn.user.model.User;
import at.ac.htlinn.user.model.UserDto;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherService {
	
	private TeacherRepository teacherRepository;
	private StudentService studentService;
	private CourseService courseService;
	private ActivityService activityService;
	private SolutionService solutionService;
	
	public User getCourseTeacher(int courseId) {
		return teacherRepository.getCourseTeacher(courseId);
	}

	public boolean isUserTeacher(int userId, int courseId) {
		return teacherRepository.isUserTeacher(userId, courseId) != 0;
	}
	
	public List<CourseDto> getTeacherView(int teacherId) {
		// courses are converted to DTOs
		return courseService.getCoursesByTeacherId(teacherId).stream()
		        .map(course -> new CourseDto(course))
		        .collect(Collectors.toList());
	}
	
	public TeacherCourseDto getCourseView(int courseId) {
		Course course = courseService.getCourseById(courseId);
		
		// students are converted to DTOs
		List<UserDto> students = studentService.getAllStudentsInCourse(courseId).stream()
		        .map(student -> new UserDto(student))
		        .collect(Collectors.toList());
		
		List<TeacherActivityDto> activityViews = new ArrayList<TeacherActivityDto>();
		// get activity view for each activity
		for (Activity activity : activityService.getAllActivitiesInCourse(courseId)) {
			
			int submitted = solutionService.getNumberOfSubmittedSolutions(activity.getId());
			int feedbacked = solutionService.getNumberOfFeedbackedSolutions(activity.getId());
			activityViews.add(new TeacherActivityDto(activity, submitted, feedbacked));
		}
		
		return new TeacherCourseDto(course, students, activityViews);
	}
}
