package at.ac.htlinn.courseManagement.courseUser;

import java.util.List;

import org.springframework.stereotype.Repository;

import at.ac.htlinn.user.model.User;

@Repository
public class CourseUserService {

	private CourseUserRepository courseUserRepository;
	
	public CourseUserService(CourseUserRepository courseUserRepository) {
		this.courseUserRepository = courseUserRepository;
	}
	
	

	public List<User> getAllStudents() {
		return courseUserRepository.getAllStudents();
	}
	
	public List<User> getAllStudentsInCourse(int courseId) {
		return courseUserRepository.getAllStudentsInCourse(courseId);
	}

	public boolean isUserStudent(int userId, int courseId) {
		return courseUserRepository.isUserStudent(userId, courseId) != 0;
	}
	


	public boolean addStudentToCourse(int courseId, int studentId) {
		try {
			// check if student is already in course
			if (isUserStudent(studentId, courseId)) {
				return false;
			}
			// check if student is already teacher
			if (isUserTeacher(studentId, courseId)) {
				return false;
			}
			courseUserRepository.addUserToCourse(studentId, courseId);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean removeStudentFromCourse(int courseId, int studentId) {
		try {
			courseUserRepository.removeUserFromCourse(studentId, courseId);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public boolean isUserInCourse(int userId, int courseId) {
		return courseUserRepository.isUserStudent(userId, courseId) != 0
				|| courseUserRepository.isUserTeacher(userId, courseId) != 0;
	}
	
	
	
	public User getCourseTeacher(int courseId) {
		return courseUserRepository.getCourseTeacher(courseId);
	}


	public boolean isUserTeacher(int userId, int courseId) {
		return courseUserRepository.isUserTeacher(userId, courseId) != 0;
	}
}
