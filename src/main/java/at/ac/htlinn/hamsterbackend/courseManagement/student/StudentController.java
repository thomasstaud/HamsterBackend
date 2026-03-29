package at.ac.htlinn.hamsterbackend.courseManagement.student;

import java.security.Principal;
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

import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.student.dto.StudentCourseDto;
import at.ac.htlinn.hamsterbackend.user.model.User;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.dto.UserDto;

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
	
	@GetMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseId(
			@RequestParam(name = "course_id", required = false) Integer courseId, Principal principal) {
		
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
		User user = userService.findUserByUsername(principal.getName());
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
		
		// get users and convert to DTOs
		List<UserDto> students = new ArrayList<UserDto>();
		for (User student : studentService.getAllStudentsInCourse(courseId)) {
			students.add(new UserDto(student));
		}
		
		return ResponseEntity.ok(students);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentsToCourse(
			@RequestParam(name = "course_id", required = true) int courseId,
			@RequestBody JsonNode node, Principal principal) throws JsonProcessingException {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.findUserByUsername(principal.getName());
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

	@DeleteMapping("{student_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> removeStudentFromCourse(
			@PathVariable(name = "student_id") int studentId,
			@RequestParam(name = "course_id", required = true) int courseId, Principal principal) {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.findUserByUsername(principal.getName());
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to remove a student.", HttpStatus.FORBIDDEN);
		
		return studentService.removeStudentFromCourse(courseId, studentId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not remove student from course!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("my-view")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getViewForLoggedInStudent(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
		
		List<StudentCourseDto> courseViews = studentService.getStudentView(user.getId());
		return ResponseEntity.ok(courseViews);
	}
}
