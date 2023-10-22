package io.github.Hattinger04.course;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.Hattinger04.course.model.CourseService;
import io.github.Hattinger04.course.model.ExerciseViewDTO;
import io.github.Hattinger04.course.model.StudentViewDTO;
import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.course.CourseDTO;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.exercise.ExerciseDTO;
import io.github.Hattinger04.course.model.solution.Solution;
import io.github.Hattinger04.course.model.solution.SolutionDTO;
import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserDTO;
import io.github.Hattinger04.user.model.UserService;

@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private UserService userService;
	@Autowired
	private ObjectMapper mapper;
	
	// TODO: check if teacher is in course when making rest request!
	// TODO: proper parameter and return documentation

	/**************************************************************
	*
	*	STUDENT MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET all students by course id
	 * requires @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseId(@PathVariable int course_id) {
		// check if course exists
		if (courseService.getCourseById(course_id) == null) {
			return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		}
		
		// get users and convert to DTOs
		List<UserDTO> students = new ArrayList<UserDTO>();
		for (User student : courseService.getAllStudentsInCourse(course_id)) {
			students.add(new UserDTO(student));
		}
		
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	/**
	 * GET student by student id from specified course
	 * requires @PathVariable student_id and course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}/students/{student_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getStudentInCourseByCourseId(@PathVariable int course_id, @PathVariable int student_id) {
		User student = userService.findUserByID(student_id);
		
		// check if course exists
		if (courseService.getCourseById(course_id) == null) {
			return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		}
		
		// check if student exists and is in course
		if (student == null || !courseService.isUserStudent(student_id, course_id)) {
			return new ResponseEntity<>("Student does not exist!", HttpStatus.NOT_FOUND);
		}
		
		// return student as DTO
		return new ResponseEntity<>(new UserDTO(student), HttpStatus.OK);
	}
	
	/**
	 * POST existing student to existing course
	 * requires @PathVariable course_id and student_id
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/{course_id}/students/{student_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentToCourse(@PathVariable int course_id, @PathVariable int student_id) {
		return courseService.addStudentToCourse(course_id, student_id) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not add student to course!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * DELETE student from course
	 * requires @PathVariable student_id and course_id
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/{course_id}/students/{student_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> removeStudentFromCourse(@PathVariable int course_id, @PathVariable int student_id) {
		return courseService.removeStudentFromCourse(course_id, student_id) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not remove student from course!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	TEACHER MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET teacher by course id
	 * requires @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}/teacher")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseTeacherByCourseID(@PathVariable int course_id) {
		User teacher = courseService.getCourseTeacher(course_id);
		if (teacher == null) {
			return new ResponseEntity<>("This course has invalid teachers - contact an admin!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(new UserDTO(teacher), HttpStatus.OK);
	}

	/**************************************************************
	*
	*	COURSE MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET course by id
	 * requires @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseById(@PathVariable int course_id) {
		Course course = courseService.getCourseById(course_id); 
		if (course == null) {
			return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new CourseDTO(course), HttpStatus.OK);
	}
	
	/**
	 * GET all courses or one course by course name
	 * optional @RequestParam course_name
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseByCourseName(@RequestParam(name = "course_name", required = false) String course_name) {
		if(course_name == null) {
			// get all courses and convert to DTOs
			List<CourseDTO> courses = new ArrayList<CourseDTO>();
			for(Course course : courseService.getAllCourses()) {
				courses.add(new CourseDTO(course));
			}
			return new ResponseEntity<>(courses, HttpStatus.OK);
		}
		Course course = courseService.getCourseByName(course_name); 
		if(course == null) {
			return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new CourseDTO(course), HttpStatus.OK);
	}
	
	/**
	 * GET all courses for a student
	 * requires @PathVariable student_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students/{student_id}/courses")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCoursesByStudentId(@PathVariable int student_id) {
		// get courses and convert to DTOs
		List<CourseDTO> courses = new ArrayList<CourseDTO>();
		for(Course course : courseService.getCoursesByStudentId(student_id)) {
			courses.add(new CourseDTO(course));
		}
		
		if(courses.isEmpty()) {
			return new ResponseEntity<>("No courses available", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}

	// TODO: check if user is student
	/**
	 * GET all courses for active user (must be student)
	 *
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students/my-courses")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCoursesForLoggedInStudent() {
		int student_id = userService.getCurrentUser().getId();
		
		// get courses and convert to DTOs
		List<CourseDTO> courses = new ArrayList<CourseDTO>();
		for(Course course : courseService.getCoursesByStudentId(student_id)) {
			courses.add(new CourseDTO(course));
		}
		
		if(courses.isEmpty()) {
			return new ResponseEntity<>("No courses available", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(courses, HttpStatus.OK);
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
		Course course = mapper.convertValue(node.get("course"), Course.class);
		course = courseService.saveCourse(course);
		if (course != null) {
			return new ResponseEntity<>(course.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<>("Could not create course!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * DELETE course
	 * requires @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/{course_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteCourse(@PathVariable int course_id) {
		return courseService.deleteCourse(course_id) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete course!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	EXERCISE MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET exercise by id
	 * requires @PathVariable exercise_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/exercises/{exercise_id}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getExerciseById(@PathVariable int exercise_id) {
		Exercise exercise = courseService.getExerciseById(exercise_id); 
		if (exercise == null) {
			return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new ExerciseDTO(exercise), HttpStatus.OK);
	}
	
	/**
	 * GET all exercises for a course
	 * requires @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}/exercises")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllExercisesByCourseId(@PathVariable int course_id) {
		if (courseService.getCourseById(course_id) == null)
			return new ResponseEntity<>("Course does not exist!", HttpStatus.BAD_REQUEST);
		
		// get exercises and convert to DTOs
		List<ExerciseDTO> exercises = new ArrayList<ExerciseDTO>();
		for(Exercise exercise : courseService.getAllExercisesInCourse(course_id)) {
			exercises.add(new ExerciseDTO(exercise));
		}
		
		if (exercises.isEmpty()) {
			return new ResponseEntity<>("Course is empty!", HttpStatus.NOT_FOUND);
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
	@PostMapping("/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		exercise = courseService.saveExercise(exercise);
		
		return exercise != null ? new ResponseEntity<>(exercise.getId(), HttpStatus.OK)
				: new ResponseEntity<>("Could not create exercise!", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * PATCH exercise
	 * requires @PathVariable exercise_id and in @RequestBody object
	 * 
	 * @param json
	 * @return
	 */
	@PatchMapping("/exercises/{exerciseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> updateExercise(@PathVariable int exerciseId, @RequestBody Map<String, Object> fields) {
		
	    // Sanitize and validate the data
	    if (exerciseId <= 0 || fields == null || fields.isEmpty()){
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    
	    Exercise exercise = courseService.getExerciseById(exerciseId);
	    
	    if (exercise == null) {
	    	return new ResponseEntity<>("Exercise does not exist!", HttpStatus.NOT_FOUND);
	    }

	    fields.forEach((k, v) -> {
		    System.out.println(k);
	        Field field = ReflectionUtils.findField(Exercise.class, k); // find field in exercise class
	        field.setAccessible(true); 
	        ReflectionUtils.setField(field, exercise, v); // set given field for exercise object to value V
	    });

	    courseService.saveExercise(exercise);
	    return new ResponseEntity<>(exercise.getId(), HttpStatus.OK);
	}

	/**
	 * DELETE existing exercise
	 * requires @PathVariable exercise_id
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/exercises/{exercise_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteExercise(@PathVariable int exercise_id) {
		return courseService.deleteExercise(exercise_id) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	SOLUTION MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET solution by id
	 * requires @PathVariable solution_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/solutions/{solution_id}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getSolutionById(@PathVariable int solution_id) {
		Solution solution = courseService.getSolutionById(solution_id); 
		if (solution == null) {
			return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(new SolutionDTO(solution), HttpStatus.OK);
	}
	
	/**
	 * GET all solutions for an exercise
	 * requires @PathVariable exercise_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/exercises/{exercise_id}/solutions")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllSolutionsByExerciseId(@PathVariable int exercise_id) {
		if (courseService.getExerciseById(exercise_id) == null)
			return new ResponseEntity<>("Exercise does not exist!", HttpStatus.BAD_REQUEST);

		// get solutions and convert to DTOs
		List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
		for (Solution solution : courseService.getSolutionsByExerciseId(exercise_id)) {
			solutions.add(new SolutionDTO(solution));
		}
		
		if (solutions.isEmpty()) {
			return new ResponseEntity<>("Exercise has no solutions!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(solutions, HttpStatus.OK);
	}

	/**
	 * GET all solutions from a student for a specified course
	 * requires @PathVariable student_id, course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}/students/{student_id}/solutions")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllSolutionsByStudentId(@PathVariable int course_id, @PathVariable int student_id) {
		if (courseService.getCourseById(course_id) == null)
			return new ResponseEntity<>("Course does not exist!", HttpStatus.BAD_REQUEST);
		if (!courseService.isUserStudent(student_id, course_id))
			return new ResponseEntity<>("Student does not exist or is not in course!", HttpStatus.BAD_REQUEST);
		
		// get solutions and convert to DTOs
		List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
		for (Solution solution : courseService.getSolutionsByStudentId(student_id, course_id)) {
			solutions.add(new SolutionDTO(solution));
		}
		
		if (solutions.isEmpty()) {
			return new ResponseEntity<>("Student has no solutions!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(solutions, HttpStatus.OK);
	}
	
	/**
	 * POST solution for an exercise
	 * requires in @RequestBody solution object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/solutions")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> addSolutionToExercise(@RequestBody JsonNode node) {
		Solution solution = mapper.convertValue(node.get("solution"), Solution.class);

		// check if different solution already exists for this exercise and student
		int exercise_id = solution.getExercise().getId();
		int student_id = solution.getStudent().getId();
		Solution existing_solution = courseService.getSolutionByExerciseAndStudentId(exercise_id, student_id);
		if (existing_solution != null && existing_solution.getId() != solution.getId())
			return new ResponseEntity<>("Solution already exists!", HttpStatus.BAD_REQUEST);
		
		solution = courseService.saveSolution(solution);
		return solution != null ? new ResponseEntity<>(solution.getId(), HttpStatus.OK)
				: new ResponseEntity<>("Could not create solution!", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Add evaluation to an exercise for one student
	 * Needs exercise object + student object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/solutions/{solution_id}/feedback")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> feedbackSolution(@PathVariable int solution_id, @RequestBody JsonNode node) {
		Solution solution = courseService.getSolutionById(solution_id);
		if (solution == null) new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);
		
		String feedback = mapper.convertValue(node.get("feedback"), String.class);
		solution.setFeedback(feedback);
		
		System.out.println(feedback);
		
	    courseService.saveSolution(solution);
	    return new ResponseEntity<>(solution.getId(), HttpStatus.OK);
	}

	/**
	 * DELETE existing solution
	 * requires @PathVariable solution_id
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/solutions/{solution_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteSolution(@PathVariable int solution_id) {
		return courseService.deleteSolution(solution_id) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	VARIOUS MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET all courses and exercises for active user (must be student)
	 *
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students/my-view")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getViewForLoggedInStudent() {
		int student_id = userService.getCurrentUser().getId();
		
		List<StudentViewDTO> studentViews = new ArrayList<StudentViewDTO>();
		// get exercises for each course
		for (Course course : courseService.getCoursesByStudentId(student_id)) {
			List<ExerciseViewDTO> exerciseViews = new ArrayList<ExerciseViewDTO>();
			// get exercise view for each exercise
			for (Exercise exercise : courseService.getAllExercisesInCourse(course.getId())) {
				Solution solution = courseService.getSolutionByExerciseAndStudentId(exercise.getId(), student_id);
				if(solution != null)
					exerciseViews.add(new ExerciseViewDTO(exercise, solution));
				else
					exerciseViews.add(new ExerciseViewDTO(exercise));
			}
			studentViews.add(new StudentViewDTO(course, exerciseViews));
		}
		
		if(studentViews.isEmpty()) {
			return new ResponseEntity<>("Student is not in any course", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(studentViews, HttpStatus.OK);
	}

	// TODO: do we need this function?
	/**
	 * Service for checking if logged in user is in course
	 * requires @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}/logged-in")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> isUserInCourse(@PathVariable int course_id) {
		Course course = courseService.getCourseById(course_id); 
		if(course == null) {
			return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST); 
		}
		int user_id = userService.getCurrentUser().getId();
		
		return new ResponseEntity<>(
			courseService.isUserInCourse(user_id, course_id),
			HttpStatus.OK
		);
	}
}
