package at.ac.htlinn.courseManagement.course;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.courseManagement.course.model.CourseDTO;
import at.ac.htlinn.courseManagement.course.model.CourseService;
import at.ac.htlinn.courseManagement.course.model.CourseViewDTO;
import at.ac.htlinn.courseManagement.exercise.model.Exercise;
import at.ac.htlinn.courseManagement.exercise.model.ExerciseDTO;
import at.ac.htlinn.courseManagement.exercise.model.ExerciseViewDTO;
import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.solution.model.SolutionDTO;
import at.ac.htlinn.role.Role;
import at.ac.htlinn.user.model.User;
import at.ac.htlinn.user.model.UserDTO;
import at.ac.htlinn.user.model.UserService;

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

	/**************************************************************
	*
	*	STUDENT MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET all students by course id
	 * requires @PathVariable courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{courseId}/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseId(@PathVariable int courseId) {
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
		}
		
		// get users and convert to DTOs
		List<UserDTO> students = new ArrayList<UserDTO>();
		for (User student : courseService.getAllStudentsInCourse(courseId)) {
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
	@GetMapping("/{courseId}/students/{studentId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getStudentInCourseByCourseId(@PathVariable int courseId, @PathVariable int studentId) {
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);

		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
		}
		
		// check if student exists and is in course
		User student = userService.findUserByID(studentId);
		if (student == null || !courseService.isUserStudent(studentId, courseId)) {
			return new ResponseEntity<>("Student does not exist!", HttpStatus.NOT_FOUND);
		}
		
		// return student as DTO
		return new ResponseEntity<>(new UserDTO(student), HttpStatus.OK);
	}
	
	/**
	 * POST existing student to existing course
	 * requires @PathVariable courseId and studentId
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/{courseId}/students/{studentId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentToCourse(@PathVariable int courseId, @PathVariable int studentId) {
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to add a student.", HttpStatus.FORBIDDEN);
		}
		
		return courseService.addStudentToCourse(courseId, studentId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not add student to course!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * DELETE student from course
	 * requires @PathVariable studentId and courseId
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/{courseId}/students/{studentId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> removeStudentFromCourse(@PathVariable int courseId, @PathVariable int studentId) {
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to remove a student.", HttpStatus.FORBIDDEN);
		}
		
		return courseService.removeStudentFromCourse(courseId, studentId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not remove student from course!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	TEACHER MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET teacher by course id
	 * requires @PathVariable courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{courseId}/teacher")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseTeacherByCourseID(@PathVariable int courseId) {
		User teacher = courseService.getCourseTeacher(courseId);
		if (teacher == null) return new ResponseEntity<>("This course has invalid teachers - contact an admin!", HttpStatus.INTERNAL_SERVER_ERROR);

		return new ResponseEntity<>(new UserDTO(teacher), HttpStatus.OK);
	}

	/**************************************************************
	*
	*	COURSE MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET course by id
	 * requires @PathVariable courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{courseId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCourseById(@PathVariable int courseId) {
		Course course = courseService.getCourseById(courseId); 
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);

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
		if(course == null) return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(new CourseDTO(course), HttpStatus.OK);
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
		Course course = new Course(courseDTO, userService);
		
		course = courseService.saveCourse(course);
		if (course != null) return new ResponseEntity<>(course.getId(), HttpStatus.OK);

		return new ResponseEntity<>("Could not create course!", HttpStatus.BAD_REQUEST);
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
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to delete it.", HttpStatus.FORBIDDEN);
		}
		
		return courseService.deleteCourse(courseId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete course!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	EXERCISE MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET exercise by id
	 * requires @PathVariable exerciseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/exercises/{exerciseId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getExerciseById(@PathVariable int exerciseId) {
		Exercise exercise = courseService.getExerciseById(exerciseId);
		if (exercise == null) return new ResponseEntity<>("Exercise does not exist!", HttpStatus.NOT_FOUND);
		
		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if user is in course (either as student or as teacher)
			if (!courseService.isUserInCourse(user.getId(), exercise.getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to view its exercises.", HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<>(new ExerciseDTO(exercise), HttpStatus.OK);
	}
	
	/**
	 * GET all exercises for a course
	 * requires @PathVariable courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{courseId}/exercises")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllExercisesByCourseId(@PathVariable int courseId) {
		// check if course exists
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);

		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if user is in course (either as student or as teacher)
			if (!courseService.isUserInCourse(user.getId(), course.getId()))
				return new ResponseEntity<>("You must be in this course to view its exercises.", HttpStatus.FORBIDDEN);
		}
		
		// get exercises and convert to DTOs
		List<ExerciseDTO> exercises = new ArrayList<ExerciseDTO>();
		for(Exercise exercise : courseService.getAllExercisesInCourse(courseId)) {
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
		ExerciseDTO exerciseDTO = mapper.convertValue(node.get("exercise"), ExerciseDTO.class);
		Exercise exercise = new Exercise(exerciseDTO, courseService);
		
		exercise = courseService.saveExercise(exercise);
		
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
	@PatchMapping("/exercises/{exerciseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> updateExercise(@PathVariable int exerciseId, @RequestBody Map<String, Object> fields) {
		
	    // Sanitize and validate the data
	    if (exerciseId <= 0 || fields == null || fields.isEmpty()){
	        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	    }
	    
	    Exercise exercise = courseService.getExerciseById(exerciseId);
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

	    courseService.saveExercise(exercise);
	    return new ResponseEntity<>(exercise.getId(), HttpStatus.OK);
	}

	/**
	 * DELETE existing exercise
	 * requires @PathVariable exerciseId
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/exercises/{exerciseId}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteExercise(@PathVariable int exerciseId) {
	    Exercise exercise = courseService.getExerciseById(exerciseId);

		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && exercise.getCourse().getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to delete its exercises.", HttpStatus.FORBIDDEN);
		}
		
		return courseService.deleteExercise(exerciseId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	SOLUTION MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET solution by id
	 * requires @PathVariable solutionId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/solutions/{solutionId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getSolutionById(@PathVariable int solutionId) {
		Solution solution = courseService.getSolutionById(solutionId); 
		if (solution == null) return new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);

		User user = userService.getCurrentUser();
		if (!(user.getRoles().contains(new Role(1, "ADMIN")) || user.getRoles().contains(new Role(2, "DEV")))) {
			// check if user is in course (either as student or as teacher)
			if (!courseService.isUserInCourse(user.getId(), solution.getExercise().getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to view its solutions.", HttpStatus.FORBIDDEN);
	
			// if user is a student, they must have created the solution
			for (Role role : user.getRoles()) {
				if (role.getRole().equals("USER") && solution.getStudent().getId() != user.getId())
					return new ResponseEntity<>("You cannot view other student's solutions.", HttpStatus.FORBIDDEN);
			}
		}
		
		return new ResponseEntity<>(new SolutionDTO(solution), HttpStatus.OK);
	}
	
	/**
	 * GET all solutions for an exercise
	 * requires @PathVariable exerciseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/exercises/{exerciseId}/solutions")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllSolutionsByExerciseId(@PathVariable int exerciseId) {
	    Exercise exercise = courseService.getExerciseById(exerciseId);
	    if (exercise == null) return new ResponseEntity<>("Exercise does not exist!", HttpStatus.NOT_FOUND);

		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && exercise.getCourse().getTeacher().getId() != user.getId()) {
				return new ResponseEntity<>("You must be this courses teacher to view its exercises.", HttpStatus.FORBIDDEN);
			}
		}

		// get solutions and convert to DTOs
		List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
		for (Solution solution : courseService.getSolutionsByExerciseId(exerciseId)) {
			solutions.add(new SolutionDTO(solution));
		}
		
		if (solutions.isEmpty()) return new ResponseEntity<>("Exercise has no solutions!", HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(solutions, HttpStatus.OK);
	}

	/**
	 * GET all solutions from a student for a specified course
	 * requires @PathVariable studentId, courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{courseId}/students/{studentId}/solutions")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllSolutionsByStudentId(@PathVariable int courseId, @PathVariable int studentId) {
		// check if course exists and student is in course
		Course course = courseService.getCourseById(courseId);
		if (course == null) return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		if (!courseService.isUserStudent(studentId, courseId))
			return new ResponseEntity<>("Student does not exist or is not in course!", HttpStatus.BAD_REQUEST);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && course.getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
		}
		
		// get solutions and convert to DTOs
		List<SolutionDTO> solutions = new ArrayList<SolutionDTO>();
		for (Solution solution : courseService.getSolutionsByStudentId(studentId, courseId)) {
			solutions.add(new SolutionDTO(solution));
		}
		
		if (solutions.isEmpty()) return new ResponseEntity<>("Student has no solutions!", HttpStatus.NOT_FOUND);

		return new ResponseEntity<>(solutions, HttpStatus.OK);
	}
	
	/**
	 * PUT solution for an exercise
	 * requires in @RequestBody solution object
	 * 
	 * @param json
	 * @return
	 */
	@PutMapping("/solutions")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> addSolutionToExercise(@RequestBody JsonNode node) {
		SolutionDTO solutionDTO = mapper.convertValue(node.get("solution"), SolutionDTO.class);
		Solution solution = new Solution(solutionDTO, courseService, userService);

		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if studentId in solution matches active user
			if (solutionDTO.getStudentId() != user.getId())
				return new ResponseEntity<>("You cannot create or update other student's solutions.", HttpStatus.FORBIDDEN);
			
			// check if user is student in course
			if (!courseService.isUserStudent(user.getId(), solution.getExercise().getCourse().getId()))
				return new ResponseEntity<>("You must be in this course to create solutions.", HttpStatus.FORBIDDEN);
		}

		if (solution.getId() != 0) {
			// update existing solution
			Solution existingSolution = courseService.getSolutionById(solution.getId());
			
			// check if id is correct
			if (existingSolution.getId() != solution.getId())
				return new ResponseEntity<>("Solution already exists with different ID!", HttpStatus.BAD_REQUEST);
			// check if there is already feedback
			if (existingSolution.getFeedback() != null)
				return new ResponseEntity<>("Can't update solution: It was already feedbacked!", HttpStatus.BAD_REQUEST);
			
			// update fields
			existingSolution.setCode(solution.getCode());
			existingSolution.setSubmitted(solution.isSubmitted());
			solution = existingSolution;
		}
		
		solution = courseService.saveSolution(solution);
		return solution != null ? new ResponseEntity<>(solution.getId(), HttpStatus.OK)
				: new ResponseEntity<>("Could not update solution!", HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * POST feedback to a solution
	 * requires @PathVariable solutionId + in @RequestBody feedback object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/solutions/{solutionId}/feedback")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> feedbackSolution(@PathVariable int solutionId, @RequestBody JsonNode node) {
		Solution solution = courseService.getSolutionById(solutionId);
		if (solution == null) new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);
		if (!solution.isSubmitted()) new ResponseEntity<>("Solution was not submitted!", HttpStatus.BAD_REQUEST);
		
		// if user is a teacher, they must be teacher of the specified course
		User user = userService.getCurrentUser();
		for (Role role : user.getRoles()) {
			if (role.getRole().equals("TEACHER") && solution.getExercise().getCourse().getTeacher().getId() != user.getId())
				return new ResponseEntity<>("You must be this courses teacher to view its students.", HttpStatus.FORBIDDEN);
		}
		
		String feedback = mapper.convertValue(node.get("feedback"), String.class);
		solution.setFeedback(feedback);
		
		System.out.println(feedback);
		
	    courseService.saveSolution(solution);
	    return new ResponseEntity<>(solution.getId(), HttpStatus.OK);
	}
	
	/**
	 * DELETE existing solution
	 * requires @PathVariable solutionId
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/solutions/{solutionId}")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> deleteSolution(@PathVariable int solutionId) {
		Solution solution = courseService.getSolutionById(solutionId);
		if (solution == null) new ResponseEntity<>("Solution does not exist!", HttpStatus.NOT_FOUND);
		
		User user = userService.getCurrentUser();
		if (!user.getRoles().contains(new Role(1, "ADMIN")) && !user.getRoles().contains(new Role(2, "DEV"))) {
			// check if solution has already been given feedback
			if (solution.getFeedback() != null)
				return new ResponseEntity<>("You cannot delete solutions that were already feedbacked.", HttpStatus.FORBIDDEN);
			
			// check if user created the solution
			if (solution.getStudent().getId() != user.getId())
				return new ResponseEntity<>("You cannot delete other student's solutions.", HttpStatus.FORBIDDEN);
		}
		
		return courseService.deleteSolution(solutionId) ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
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
		int studentId = userService.getCurrentUser().getId();
		
		List<CourseViewDTO> courseViews = new ArrayList<CourseViewDTO>();
		// get exercises for each course
		for (Course course : courseService.getCoursesByStudentId(studentId)) {
			List<ExerciseViewDTO> exerciseViews = new ArrayList<ExerciseViewDTO>();
			// get exercise view for each exercise
			for (Exercise exercise : courseService.getAllExercisesInCourse(course.getId())) {
				Solution solution = courseService.getSolutionByExerciseAndStudentId(exercise.getId(), studentId);
				
				if(solution != null)
					exerciseViews.add(new ExerciseViewDTO(exercise, solution));
				else
					exerciseViews.add(new ExerciseViewDTO(exercise));
			}
			courseViews.add(new CourseViewDTO(course, exerciseViews));
		}
		
		if(courseViews.isEmpty()) return new ResponseEntity<>("Student is not in any course", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<>(courseViews, HttpStatus.OK);
	}

	// TODO: do we need this function?
	/**
	 * Service for checking if logged in user is in course
	 * requires @PathVariable courseId
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{courseId}/logged-in")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> isUserInCourse(@PathVariable int courseId) {
		Course course = courseService.getCourseById(courseId); 
		if(course == null) return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST); 

		int userId = userService.getCurrentUser().getId();
		
		return new ResponseEntity<>(
			courseService.isUserInCourse(userId, courseId),
			HttpStatus.OK
		);
	}
}
