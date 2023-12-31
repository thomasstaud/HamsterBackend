package at.ac.htlinn.courseManagement.course;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.course.model.CourseDTO;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;

@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	// TODO: clean up admin/dev permissions
	// TODO: proper parameter and return documentation
	
	/**
	 * GET course by id
	 * requires @PathVariable courseId
	 * 
	 * @param	courseId
	 * @return	course
	 */
	@GetMapping("{courseId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseById(@PathVariable int courseId) {
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		return ResponseEntity.ok(new CourseDTO(course));
	}
	
	/**
	 * GET all courses or one course by course name
	 * optional @RequestParam course_name
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseByCourseName(
			@RequestParam(name = "course_name", required = false) String course_name) {
		
		if(course_name == null) {
			// get all courses and convert to DTOs
			List<CourseDTO> courses = new ArrayList<CourseDTO>();
			for(Course course : courseService.getAllCourses()) {
				courses.add(new CourseDTO(course));
			}
			return ResponseEntity.ok(courses);
		}
		
		// get one course by name
		Course course = courseService.getCourseByName(course_name); 
		if(course == null) return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);

		return ResponseEntity.ok(new CourseDTO(course));
	}
	
	/**
	 * POST course
	 * requires in @RequestBody course object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createCourse(@RequestBody JsonNode node) {
		CourseDTO courseDTO = mapper.convertValue(node.get("course"), CourseDTO.class);
		if (courseDTO == null) return new ResponseEntity<>("Request body is invalid!", HttpStatus.BAD_REQUEST);
		
		// set teacher id to active user id
		int user_id = userService.getCurrentUser().getId();
		courseDTO.setTeacherId(user_id);
		Course course = new Course(courseDTO, userService);

		return courseService.saveCourse(course) != null ? ResponseEntity.ok(course.getId())
				: new ResponseEntity<>("Could not create course!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	/**
	 * PATCH course
	 * requires @PathVariable courseId and in @RequestBody object
	 * 
	 * @param json
	 * @return
	 */
	@PatchMapping("{courseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> updateCourse(@PathVariable int courseId, @RequestBody Map<String, Object> fields) {
		
	    // Sanitize and validate the data
	    if (courseId <= 0 || fields == null || fields.isEmpty()){
	        return new ResponseEntity<>("Request body is invalid!", HttpStatus.BAD_REQUEST);
	    }

	    // must unproxy in order for reflection to work
	    Course course = courseService.getCourseById(courseId);
	    if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);

		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this course's teacher to make changes to it.", HttpStatus.FORBIDDEN);

    	/* alternative method:
    	 * TODO: advantages/disadvantages?
		try {
	    	Field field = Course.class.getDeclaredField(k);
	    	field.setAccessible(true);
	    	System.out.println(course);
	    	field.set(course, v);
	    	System.out.println(course);
		}
		catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
		*/
		
		// map fields from request body to fields from the class
		Map<Field, Object> classFields = fields.entrySet()
			    .stream()
			    .collect(Collectors.toMap(e -> ReflectionUtils.findField(Course.class, e.getKey()), e -> e.getValue()));
		
		// TODO: specify which field is invalid
		if (classFields.containsKey(null))
			return new ResponseEntity<>("Request body contains invalid fields!", HttpStatus.BAD_REQUEST);
		
	    classFields.forEach((field, value) -> {
	    	field.setAccessible(true);
	        ReflectionUtils.setField(field, course, value);
	    });

	    return courseService.saveCourse(course) != null ? ResponseEntity.ok(course.getId())
	    		: new ResponseEntity<>("Could not update course!", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * DELETE course
	 * requires @PathVariable courseId
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/{courseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteCourse(@PathVariable int courseId) {
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		if (!userService.isUserPrivileged(user) && course.getTeacher().getId() != user.getId())
			return new ResponseEntity<>("You must be this courses teacher to delete it.", HttpStatus.FORBIDDEN);
		
		return courseService.deleteCourse(courseId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete course!", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
