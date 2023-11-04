package at.ac.htlinn.courseManagement.exercise;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import at.ac.htlinn.courseManagement.course.CourseService;
import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.courseUser.CourseUserService;
import at.ac.htlinn.courseManagement.exercise.model.Exercise;
import at.ac.htlinn.courseManagement.exercise.model.ExerciseDTO;
import at.ac.htlinn.role.Role;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private ExerciseService exerciseService;
	@Autowired
	private CourseUserService studentService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	/**
	 * GET exercise by id
	 * requires @PathVariable exerciseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("{exerciseId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getExerciseById(@PathVariable int exerciseId) {
		Exercise exercise = exerciseService.getExerciseById(exerciseId);
		if (exercise == null) return new ResponseEntity<>("Exercise does not exist!", HttpStatus.NOT_FOUND);
		
		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if user is in course (either as student or as teacher)
			if (!studentService.isUserInCourse(user.getId(), exercise.getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to view its exercises.", HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<>(new ExerciseDTO(exercise), HttpStatus.OK);
	}
	
	/**
	 * GET all exercises for a course
	 * requires @RequestParam courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllExercisesByCourseId(
			@RequestParam(name = "course_id", required = true) Integer courseId) {
		
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);

		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if user is in course (either as student or as teacher)
			if (!studentService.isUserInCourse(user.getId(), course.getId()))
				return new ResponseEntity<>("You must be in this course to view its exercises.", HttpStatus.FORBIDDEN);
		}
		
		List<ExerciseDTO> exercises = new ArrayList<ExerciseDTO>();
		for (Exercise exercise : exerciseService.getAllExercisesInCourse(courseId)) {
			exercises.add(new ExerciseDTO(exercise));
		}
		return new ResponseEntity<>(exercises, HttpStatus.OK);
	}
	
	/**
	 * POST exercise for existing course
	 * requires in @RequestBody exercise object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createExercise(@RequestBody JsonNode node) {
		ExerciseDTO exerciseDTO = mapper.convertValue(node.get("exercise"), ExerciseDTO.class);
		Exercise exercise = new Exercise(exerciseDTO, courseService);
		
		exercise = exerciseService.saveExercise(exercise);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && exercise.getCourse().getTeacher().getId() != user.getId()) {
				return new ResponseEntity<>("You must be this courses teacher to add an exercise to it.", HttpStatus.FORBIDDEN);
			}
		}
		
		return exercise != null ? new ResponseEntity<>(exercise.getId(), HttpStatus.OK)
				: new ResponseEntity<>("Could not create exercise!", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * PATCH exercise
	 * requires @PathVariable exerciseId and in @RequestBody object
	 * 
	 * @param json
	 * @return
	 */
	@PatchMapping("{exerciseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> updateExercise(@PathVariable int exerciseId, @RequestBody Map<String, Object> fields) {
		
	    // Sanitize and validate the data
	    if (exerciseId <= 0 || fields == null || fields.isEmpty()){
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    
	    Exercise exercise = exerciseService.getExerciseById(exerciseId);
	    if (exercise == null) return new ResponseEntity<>("Exercise does not exist!", HttpStatus.NOT_FOUND);

		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && exercise.getCourse().getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to change its exercises.", HttpStatus.FORBIDDEN);
		}

	    fields.forEach((k, v) -> {
		    System.out.println(k);
	        Field field = ReflectionUtils.findField(Exercise.class, k); // find field in exercise class
	        field.setAccessible(true); 
	        ReflectionUtils.setField(field, exercise, v); // set given field for exercise object to value V
	    });

	    exerciseService.saveExercise(exercise);
	    return new ResponseEntity<>(exercise.getId(), HttpStatus.OK);
	}

	/**
	 * DELETE existing exercise
	 * requires @PathVariable exerciseId
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("{exerciseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteExercise(@PathVariable int exerciseId) {
	    Exercise exercise = exerciseService.getExerciseById(exerciseId);

		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && exercise.getCourse().getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to delete its exercises.", HttpStatus.FORBIDDEN);
		}
		
		return exerciseService.deleteExercise(exerciseId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.BAD_REQUEST);
	}
}
