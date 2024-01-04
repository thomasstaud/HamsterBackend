package at.ac.htlinn.courseManagement.student;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.activity.ActivityService;
import at.ac.htlinn.courseManagement.activity.model.Activity;
import at.ac.htlinn.courseManagement.activity.model.Contest;
import at.ac.htlinn.courseManagement.activity.model.Exercise;
import at.ac.htlinn.courseManagement.course.CourseService;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.solution.SolutionService;
import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.student.model.StudentActivityDto;
import at.ac.htlinn.courseManagement.student.model.StudentContestDto;
import at.ac.htlinn.courseManagement.student.model.StudentCourseDto;
import at.ac.htlinn.courseManagement.student.model.StudentExerciseDto;
import at.ac.htlinn.courseManagement.student.model.StudentSolutionDto;
import at.ac.htlinn.user.model.User;
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
			// check if student is already teacher
//			if (isUserTeacher(studentId, courseId)) {
//				return false;
//			}
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

	public boolean removeAllStudentFromCourse(int courseId) {
		try {
			studentRepository.removeAllUsersFromCourse(courseId);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	public List<StudentCourseDto> getStudentView(int studentId) {
		
		List<StudentCourseDto> courseViews = new ArrayList<StudentCourseDto>();
		// get activities for each course
		for (Course course : courseService.getCoursesByStudentId(studentId)) {
			List<StudentActivityDto> activityViews = new ArrayList<StudentActivityDto>();
			// get activity view for each activity
			for (Activity activity : activityService.getAllActivitiesInCourse(course.getId())) {
				Solution solution = solutionService.getSolutionByActivityAndStudentId(activity.getId(), studentId);
				StudentSolutionDto solutionDto = solution != null ?
						new StudentSolutionDto(solution) : null;
				
				// add exercise or contest to list
				if (activity instanceof Exercise)
					activityViews.add(new StudentExerciseDto((Exercise)activity, solutionDto));
				else
					activityViews.add(new StudentContestDto((Contest)activity, solutionDto));
			}
			courseViews.add(new StudentCourseDto(course, activityViews));
		}
		
		return courseViews;
	}
}
