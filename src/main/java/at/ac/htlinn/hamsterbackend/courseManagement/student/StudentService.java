package at.ac.htlinn.hamsterbackend.courseManagement.student;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.ActivityService;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Contest;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.SolutionService;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.model.Solution;
import at.ac.htlinn.hamsterbackend.courseManagement.student.dto.StudentActivityDto;
import at.ac.htlinn.hamsterbackend.courseManagement.student.dto.StudentContestDto;
import at.ac.htlinn.hamsterbackend.courseManagement.student.dto.StudentCourseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.student.dto.StudentExerciseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.student.dto.StudentSolutionDto;
import at.ac.htlinn.hamsterbackend.user.model.User;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentService {
	
	private StudentRepository studentRepository;
	private CourseService courseService;
	private ActivityService activityService;
	private SolutionService solutionService;
	
	public List<User> getAllStudents() {
		return studentRepository.getAllStudents();
	}
	
	public List<User> getAllStudentsInCourse(int courseId) {
		return studentRepository.getAllStudentsInCourse(courseId);
	}
	
	public boolean isUserStudent(int userId, int courseId) {
		return studentRepository.isUserStudent(userId, courseId) != 0;
	}
	
	
	
	public boolean addStudentToCourse(int courseId, int studentId) {
		try {
			// check if student is already in course
			if (isUserStudent(studentId, courseId)) {
				return false;
			}
			studentRepository.addUserToCourse(studentId, courseId);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	public boolean removeStudentFromCourse(int courseId, int studentId) {
		try {
			studentRepository.removeUserFromCourse(studentId, courseId);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean removeAllStudentsFromCourse(int courseId) {
		try {
			studentRepository.removeAllUsersFromCourse(courseId);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	private StudentActivityDto getStudentActivityDto(Activity activity, int studentId) {
		Solution solution = solutionService.getSolutionByActivityAndStudentId(activity.getId(), studentId);
		StudentSolutionDto solutionDto = solution != null ?
				new StudentSolutionDto(solution) : null;
		
		// return either exercise or contest
		if (activity instanceof Exercise)
			return new StudentExerciseDto((Exercise)activity, solutionDto);
		else
			return new StudentContestDto((Contest)activity, solutionDto);
	}
	
	public List<StudentCourseDto> getStudentView(int studentId) {
		
		List<StudentCourseDto> courseViews = new ArrayList<StudentCourseDto>();
		// get activities for each course
		for (Course course : courseService.getCoursesByStudentId(studentId)) {
			List<StudentActivityDto> activityViews = new ArrayList<StudentActivityDto>();

			// get activity view for each visible activity
			activityService.getAllActivitiesInCourse(course.getId()).stream()
				.filter(activity -> !activity.isHidden())
				.forEach(activity -> activityViews.add(
						getStudentActivityDto(activity, studentId))
				);
			
			courseViews.add(new StudentCourseDto(course, activityViews));
		}
		
		return courseViews;
	}
}
