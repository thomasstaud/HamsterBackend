package at.ac.htlinn.hamsterbackend.courseManagement.course;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.course.dto.CourseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.student.StudentService;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	@GetMapping("{courseId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseById(@PathVariable int courseId) {
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		return ResponseEntity.ok(new CourseDto(course));
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllCourses() {
		// get all courses and convert to DTOs
		List<CourseDto> courses = new ArrayList<CourseDto>();
		for(Course course : courseService.getAllCourses()) {
			courses.add(new CourseDto(course));
		}
		return ResponseEntity.ok(courses);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createCourse(@RequestBody JsonNode node, Principal principal) {
		CourseDto courseDto = mapper.convertValue(node.get("course"), CourseDto.class);
		if (courseDto == null) return new ResponseEntity<>("Request body is invalid!", HttpStatus.BAD_REQUEST);
		
		// set teacher id to active user id
		User user = userService.findUserByUsername(principal.getName());
		courseDto.setTeacherId(user.getId());
		Course course = new Course(courseDto, userService);
		
		course = courseService.saveCourse(course);
		return course != null ? ResponseEntity.ok(course.getId())
				: new ResponseEntity<>("Could not create course!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PatchMapping("{courseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> updateCourse(@PathVariable int courseId, @RequestBody Map<String, Object> fields, Principal principal) {
		
	    // Sanitize and validate the data
	    if (courseId <= 0 || fields == null || fields.isEmpty()){
	        return new ResponseEntity<>("Request body is invalid!", HttpStatus.BAD_REQUEST);
	    }

	    Course course = courseService.getCourseById(courseId);
	    if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);

		// if user is a teacher, they must be teacher of the specified course
		User user = userService.findUserByUsername(principal.getName());
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this course's teacher to make changes to it.", HttpStatus.FORBIDDEN);
		
		try {
			course = courseService.updateCourse(course, fields);
			return course != null ? ResponseEntity.ok(course.getId())
					: new ResponseEntity<>("Could not update course!", HttpStatus.INTERNAL_SERVER_ERROR);
			
		} catch (NoSuchFieldException e) {
			return new ResponseEntity<>(String.format("Field '%s' is invalid!", e.getMessage()),
					HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(String.format("Field '%s' could not be changed!", e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("{courseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteCourse(@PathVariable int courseId, Principal principal) {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.findUserByUsername(principal.getName());
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to delete it.", HttpStatus.FORBIDDEN);
		
		// attempt to remove all students from the course
		if (!studentService.removeAllStudentsFromCourse(courseId))
			return new ResponseEntity<>("Could not remove all students from course!", HttpStatus.INTERNAL_SERVER_ERROR);
		
		return courseService.deleteCourse(course) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete course!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
