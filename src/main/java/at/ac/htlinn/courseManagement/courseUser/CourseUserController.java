package at.ac.htlinn.courseManagement.courseUser;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.ac.htlinn.courseManagement.course.CourseService;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.course.model.CourseViewDTO;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;
import at.ac.htlinn.user.model.UserDTO;

@RestController
@RequestMapping("/users")
public class CourseUserController {

	@Autowired
	private CourseUserService courseUserService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * GET all courses, exercises and solutions for active user (must be student)
	 *
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("students/my-view")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getViewForLoggedInStudent() {
		int studentId = userService.getCurrentUser().getId();
		
		List<CourseViewDTO> courseViews = courseService.getStudentCourseViews(studentId);
		return new ResponseEntity<>(courseViews, HttpStatus.OK);
	}
	
	/**
	 * GET all students or all students for one course
	 * optional @RequestParam courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseId(
			@RequestParam(name = "course_id", required = false) Integer courseId) {
		
		if (courseId == null) {
			// get all students
			
			// get users and convert to DTOs
			List<UserDTO> students = new ArrayList<UserDTO>();
			for (User student : courseUserService.getAllStudents()) {
				students.add(new UserDTO(student));
			}
			
			return new ResponseEntity<>(students, HttpStatus.OK);
		}
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
		
		// get users and convert to DTOs
		List<UserDTO> students = new ArrayList<UserDTO>();
		for (User student : courseUserService.getAllStudentsInCourse(courseId)) {
			students.add(new UserDTO(student));
		}
		
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	/**
	 * GET student by student id from specified course
	 * requires @PathVariable studentId and courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("students/{student_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getStudentInCourseByCourseId(
			@PathVariable(name = "student_id") int studentId,
			@RequestParam(name = "course_id", required = true) int courseId) {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);

		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
		
		// check if student exists and is in course
		User student = userService.findUserByID(studentId);
		if (student == null || !courseUserService.isUserStudent(studentId, courseId)) {
			return new ResponseEntity<>("Student does not exist!", HttpStatus.NOT_FOUND);
		}
		
		// return student as DTO
		return new ResponseEntity<>(new UserDTO(student), HttpStatus.OK);
	}
	
	/**
	 * POST existing student to existing course
	 * requires @RequestParam courseId and in @RequestBody student list
	 * 
	 * @param json
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PostMapping("students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentsToCourse(
			@RequestParam(name = "course_id", required = true) int courseId,
			@RequestBody JsonNode node) throws JsonProcessingException {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to add a student.", HttpStatus.FORBIDDEN);
		
		// try to add all students to the course
		int[] userIds = mapper.convertValue(node.get("users"), int[].class);
		ArrayList<Integer> failedUserIds = new ArrayList<Integer>(); 
		for (int userId : userIds) {
			boolean success = courseUserService.addStudentToCourse(courseId, userId);
			if (!success) failedUserIds.add(userId);
		}
		
		if (failedUserIds.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		
		// return list of students who could not be added
		node = mapper.valueToTree(failedUserIds);
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.set("failed_users", node);
		String json = mapper.writeValueAsString(objectNode);
		return  new ResponseEntity<>(json, HttpStatus.BAD_REQUEST);
	}

	/**
	 * DELETE student from course
	 * requires @PathVariable studentId and courseId
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("students/{student_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> removeStudentFromCourse(
			@PathVariable(name = "student_id") int studentId,
			@RequestParam(name = "course_id", required = true) int courseId) {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to remove a student.", HttpStatus.FORBIDDEN);
		
		return courseUserService.removeStudentFromCourse(courseId, studentId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not remove student from course!", HttpStatus.BAD_REQUEST);
	}
	
	
	

	/**
	 * GET teacher by course id
	 * requires @PathVariable courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("teachers")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseTeacherByCourseID(
			@RequestParam(name = "course_id", required = true) Integer courseId) {
		
		User teacher = courseUserService.getCourseTeacher(courseId);
		return new ResponseEntity<>(new UserDTO(teacher), HttpStatus.OK);
	}
	
	/**
	 * GET all courses and exercises for active user (must be teacher)
	 *
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("teachers/my-view")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getViewForLoggedInTeacher() {
		int studentId = userService.getCurrentUser().getId();
		
		List<CourseViewDTO> courseViews = courseService.getTeacherCourseViews(studentId);
		return new ResponseEntity<>(courseViews, HttpStatus.OK);
	}
}
