package io.github.Hattinger04.course;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.Hattinger04.course.model.CourseService;
import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.solution.Solution;
import io.github.Hattinger04.course.model.student.Student;
import io.github.Hattinger04.course.model.teacher.Teacher;
import io.github.Hattinger04.user.model.User;
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
	// TODO: remove private information (user password) from returned data

	/**************************************************************
	*
	*	STUDENT MAPPINGS
	*
	**************************************************************/
	
	/**
	 * GET all students
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getAllStudents() {
		List<User> students = courseService.getAllStudents(); 
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	/**
	 * GET student by id
	 * requires @PathVariable student_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students/{student_id}")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getStudentByID(@PathVariable int student_id) {
		Student student = courseService.getStudentByStudentId(student_id); 
		if (student == null || courseService.getUserByStudent(student) == null) {
			return new ResponseEntity<>("Student does not exist!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	
	/**
	 * GET all students by course id
	 * requires @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseID(@PathVariable int course_id) {
		List<Student> students;
		if ((students = courseService.getAllStudentsInCourse(course_id)) == null) {
			return new ResponseEntity<>("Course is empty or does not exist!", HttpStatus.NOT_FOUND);
		}
		students.stream().map(user -> {
	        user.getUser().setPassword("");
	        return user;
	    }).collect(Collectors.toList());
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
	public ResponseEntity<?> getStudentInCourseByCourseID(@PathVariable int course_id, @PathVariable int student_id) {
		Student student = courseService.getStudentByStudentId(student_id); 
		Course course = courseService.getCourseByID(course_id); 
		User user = courseService.getUserByStudent(student); 
		if (course == null) {
			return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		}
		if (student == null || user == null || !courseService.isUserInCourse(course, user)) {
			return new ResponseEntity<>("Student does not exist!", HttpStatus.NOT_FOUND);
		}
		student.getUser().setPassword(""); 
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	
	/**
	 * POST existing student to existing course
	 * requires @PathVariable course_id and student_id
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/{course_id}/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentToCourse(@PathVariable int course_id, @RequestBody JsonNode node) {
		Student student = mapper.convertValue(node.get("student"), Student.class);
		return courseService.addStudentToCourse(course_id, student) ? new ResponseEntity<>(HttpStatus.OK)
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
		List<User> teachers = courseService.getCourseTeachers(course_id);
		if (teachers == null || teachers.size() == 0) {
			return new ResponseEntity<>("There is no teacher in this course - contact an admin!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (teachers.size() > 1) {
			return new ResponseEntity<>("This course has multiple teachers - contact an admin!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(teachers.get(0), HttpStatus.OK);
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
	public ResponseEntity<?> getCourseByID(@PathVariable int course_id) {
		Course course = courseService.getCourseByID(course_id); 
		if (course == null) {
			return new ResponseEntity<>("Course does not exist!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
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
	public ResponseEntity<?> getCourseByCoursename(@RequestParam(name = "course_name", required = false) String course_name) {
		if(course_name == null) {
			return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
		}
		Course course = courseService.getCourseByName(course_name); 
		if(course == null) {
			return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
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
		List<Course> courses = courseService.getCoursesByStudentId(student_id); 
		if(courses == null) {
			return new ResponseEntity<>("No courses available", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}

	// TODO: remove private information (user password) from returned data
	/**
	 * GET all courses for logged in student
	 *
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students/my-courses")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getCoursesForLoggedInStudent() {
		User user = userService.getCurrentUser();
		int student_id = courseService.getStudentByUserId(user.getId()).getId();
		List<Course> courses = courseService.getCoursesByStudentId(student_id); 
		if(courses == null) {
			return new ResponseEntity<>("No courses available", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(courses, HttpStatus.OK);
	}
	
	/**
	 * POST course
	 * requires in @RequestBody course and teacher objects
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Teacher teacher = mapper.convertValue(node.get("teacher"), Teacher.class);
		if (courseService.createCourse(course) != null) {
			courseService.setCourseTeacher(course, teacher);
			return new ResponseEntity<>(HttpStatus.OK);
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
	 * GET all exercises for a course
	 * requires @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/{course_id}/exercises")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> getAllExercisesByCourseId(@PathVariable int course_id) {
		List<Exercise> exercises = courseService.getAllExercisesInCourse(course_id);
		if (exercises == null) {
			if (courseService.getCourseByID(course_id) == null)
				return new ResponseEntity<>("Course does not exist!", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(exercises, HttpStatus.OK);
	}
	
	// TODO: how are exercises created?
	// course_id is currently not used
	/**
	 * POST exercise for existing course
	 * requires @PathVariable course_id and in @RequestBody object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/{course_id}/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createExercise(@PathVariable int course_id, @RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		return courseService.createExercise(exercise) != null ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not create exercise!", HttpStatus.BAD_REQUEST);
	}
	
	// TODO: actually update instead of creating a new exercise
	// course_id is currently not used
	/**
	 * PUT exercise
	 * requires @PathVariable course_id and exercise_id and in @RequestBody object
	 * 
	 * @param json
	 * @return
	 */
	@PutMapping("/{course_id}/exercises/{exercise_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> updateExercise(@PathVariable int course_id, @PathVariable int exercise_id, @RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		return courseService.createExercise(exercise) != null ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not update exercise!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * DELETE existing exercise
	 * requires @PathVariable course_id and exercise_id
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/exercises/{exercise_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteExercise(@PathVariable int course_id, @PathVariable int exercise_id) {
		return courseService.deleteExercise(exercise_id) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	SOLUTION MAPPINGS
	*
	**************************************************************/
	
	// TODO: remove private information (user password) from returned data
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
		List<Solution> solutions = courseService.getSolutionsByExerciseId(exercise_id);
		if (solutions == null) {
			if (courseService.getExerciseByID(exercise_id) == null)
				return new ResponseEntity<>("Exercise does not exist!", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(solutions, HttpStatus.OK);
	}

	// TODO: remove private information (user password) from returned data
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
		List<Solution> solutions = courseService.getSolutionsByStudentId(student_id, course_id);
		if (solutions == null) {
			if (courseService.getCourseByID(course_id) == null)
				return new ResponseEntity<>("Course does not exist!", HttpStatus.BAD_REQUEST);
			if (courseService.getStudentByStudentId(student_id) == null)
				return new ResponseEntity<>("Student does not exist!", HttpStatus.BAD_REQUEST);
			return new ResponseEntity<>("Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
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
	@PostMapping("/exercises/{exercise_id}/solutions")
	@PreAuthorize("hasAuthority('USER')")
	public ResponseEntity<?> addSolutionToExercise(@PathVariable int exercise_id, @RequestBody JsonNode node) {
		Solution solution = mapper.convertValue(node.get("solution"), Solution.class);
		courseService.createSolution(solution);
		return courseService.createSolution(solution) != null ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not create solution!", HttpStatus.BAD_REQUEST);
	}

	/**************************************************************
	*
	*	OTHER MAPPINGS
	*
	**************************************************************/
	
	// TODO: this doesn't make any sense
	//			there is no parameter for the evaluation itself
	//			should there be a new DB table for evaluations?
	/**
	 * Add evaluation to an exercise for one student
	 * Needs exercise object + student object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/exercises/evaluate")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> evaluateExercise(@RequestBody JsonNode node) {
		// TODO: implement
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	// TODO: change url
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
		Course course = courseService.getCourseByID(course_id); 
		if(course == null) {
			return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST); 
		}
		User user = userService.getCurrentUser();
		
		return new ResponseEntity<>(
			courseService.isUserInCourse(course, user),
			HttpStatus.OK
		);
	}
}
