package at.ac.htlinn.courseManagement.student;

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
import at.ac.htlinn.courseManagement.student.model.StudentCourseDto;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;
import at.ac.htlinn.user.model.UserDto;

@RestController
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentService studentService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * GET all students or all students for one course
	 * optional @RequestParam courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseId(
			@RequestParam(name = "course_id", required = false) Integer courseId) {
		
		if (courseId == null) {
			// get all students
			
			// get users and convert to DTOs
			List<UserDto> students = new ArrayList<UserDto>();
			for (User student : studentService.getAllStudents()) {
				students.add(new UserDto(student));
			}
			
			return ResponseEntity.ok(students);
		}
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
		
		// get users and convert to DTOs
		List<UserDto> students = new ArrayList<UserDto>();
		for (User student : studentService.getAllStudentsInCourse(courseId)) {
			students.add(new UserDto(student));
		}
		
		return ResponseEntity.ok(students);
	}
	
	/**
	 * POST existing student to existing course
	 * requires @RequestParam courseId and in @RequestBody student list
	 * 
	 * @param json
	 * @return
	 * @throws JsonProcessingException 
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentsToCourse(
			@RequestParam(name = "course_id", required = true) int courseId,
			@RequestBody JsonNode node) throws JsonProcessingException {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to add a student.", HttpStatus.FORBIDDEN);
		
		// try to add all students to the course
		int[] userIds = mapper.convertValue(node.get("users"), int[].class);
		if (userIds == null) return new ResponseEntity<>("Request body is invalid", HttpStatus.BAD_REQUEST);
		
		ArrayList<Integer> failedUserIds = new ArrayList<Integer>(); 
		for (int userId : userIds) {
			boolean success = studentService.addStudentToCourse(courseId, userId);
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
	@DeleteMapping("{student_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> removeStudentFromCourse(
			@PathVariable(name = "student_id") int studentId,
			@RequestParam(name = "course_id", required = true) int courseId) {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to remove a student.", HttpStatus.FORBIDDEN);
		
		return studentService.removeStudentFromCourse(courseId, studentId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not remove student from course!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

	
	/**
	 * GET all courses, exercises and solutions for active user (must be student)
	 *
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("my-view")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getViewForLoggedInStudent() {
		int userId = userService.getCurrentUser().getId();
		
		List<StudentCourseDto> courseViews = studentService.getStudentView(userId);
		return ResponseEntity.ok(courseViews);
	}
}
