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
import io.github.Hattinger04.course.model.student.Student;
import io.github.Hattinger04.course.model.teacher.Teacher;
import io.github.Hattinger04.user.model.User;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private ObjectMapper mapper;
	
	// TODO: check if teacher is in course when making rest request!
	
	/**
	 * Get all students from database
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getStudents() {
		List<User> students = courseService.getAllStudents(); 
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
		
	
	/**
	 * Get student by id from database
	 * Needs as @PathVariable student_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students/{id}")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getStudentByID(@PathVariable long id) {
		Student student = courseService.getStudentByID((int)id); 
		if (student == null || courseService.getUserByStudent(student) == null) {
			return new ResponseEntity<>("Student does not exist!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	
	/**
	 * Get all students in Course from database
	 * Needs as @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/courses/{course_id}/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseID(@PathVariable long course_id) {
		Course course = courseService.getCourseByID((int) course_id); 
		List<Student> students;
		if ((students = courseService.getAllStudentsInCourse(course)) == null) {
			return new ResponseEntity<>("Course is empty or does not exist!", HttpStatus.NOT_FOUND);
		}
		students.stream().map(user -> {
	        user.getUser().setPassword("");
	        return user;
	    }).collect(Collectors.toList());
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
		

	/**
	 * Get student from course by id from database 
	 * Needs as @PathVariable student_id and course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/courses/{course_id}/students/{student_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getStudentInCourseByCourseID(@PathVariable long course_id, @PathVariable long student_id) {
		Student student = courseService.getStudentByID((int) student_id); 
		Course course = courseService.getCourseByID((int) course_id); 
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
	 * Get all students in Course from database
	 * Needs as @RequestParam course_name
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/courses/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseName(@RequestParam(name = "course_name", required = false) String course_name) {
		Course course = courseService.getCourseByName(course_name); 
		List<Student> students;
		if ((students = courseService.getAllStudentsInCourse(course)) == null) {
			return new ResponseEntity<>("Course is empty or does not exist!", HttpStatus.NOT_FOUND);
		}
		students.stream().map(user -> {
	        user.getUser().setPassword("");
	        return user;
	    }).collect(Collectors.toList());
		return new ResponseEntity<>(students, HttpStatus.OK);
	}

	/**
	 * Adds student to existing Course
	 * Needs course object + student object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/courses/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Student student = mapper.convertValue(node.get("student"), Student.class);
		return courseService.addStudentToCourse(course, student) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not add student to course!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Removes student from Course
	 * Needs course object + student object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/courses/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> removeStudentCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Student student = mapper.convertValue(node.get("student"), Student.class);
		return courseService.removeStudentFromCourse(course, student) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not remove student from course!", HttpStatus.NOT_MODIFIED);
	}
	
	/**
	 * Get teacher from course from database
	 * Needs as @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/courses/{course_id}/teachers")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseTeacherByCourseID(@PathVariable long course_id) {
		Course course = courseService.getCourseByID((int) course_id); 
		List<User> teachers;
		if ((teachers = courseService.getCourseTeachers(course)) == null) {
			return new ResponseEntity<>("There is no teacher in this course - contact an admin!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	
	/**
	 * get teacher from course from database
	 * Needs as @RequestParam course_name
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/courses/teachers")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseTeacherByCourseName(@RequestParam(name = "course_name", required = false) String course_name) {
		Course course = courseService.getCourseByName(course_name); 
		List<User> teachers;
		if ((teachers = courseService.getCourseTeachers(course)) == null) {
			return new ResponseEntity<>("There is no teacher in this course - contact an admin!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	/**
	 * Get course from database
	 * Needs as @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/courses/{course_id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseByID(@PathVariable long course_id) {
		Course course = courseService.getCourseByID((int) course_id); 
		if (course == null) {
			return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	
	/**
	 * Returns all or one course
	 * Needs as @RequestParam course_name
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/courses")
	@PreAuthorize("hasAuthority('TEACHER')")
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
	 * Create a new Course
	 * Needs course and teacher object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/courses")
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
	 * Deletes an existing Course
	 * Needs course object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/courses")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		return courseService.deleteCourse(course) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete course!", HttpStatus.NOT_MODIFIED);
	}

	
	// TODO: GetMapping for exercises
	

	/**
	 * Creates a new exercise in a existing Course
	 * Needs course object + exercise
	 * object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		return courseService.createExercise(exercise) != null ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not create exercise!", HttpStatus.BAD_REQUEST);
	}
	
	// TODO: actually update instead of creating a new exercise
	/**
	 * Changes already existing (and published) exercises
	 * Needs exercise object
	 * 
	 * @param json
	 * @return
	 */
	@PutMapping("/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> updateExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		return courseService.createExercise(exercise) != null ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not update exercise!", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Deletes existing exercise
	 * Needs exercise object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		return courseService.deleteExercise(exercise) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.NOT_MODIFIED);
	}
	
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
		// TODO: evaluate exercise from student
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	// TODO: change url
	// TODO: do we need this function? if yes, HTTP return codes shouldn't be used to return information
	/**
	 * Service for checking if logged in user is in course
	 * Needs course object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/isUserInCourse")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> isUserInCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		if(course == null) {
			return new ResponseEntity<>("Course not found!", HttpStatus.BAD_REQUEST); 
		}
		User user = mapper.convertValue(node.get("user"), User.class); 
		if(courseService.isUserInCourse(course, user)) {
			return new ResponseEntity<>(HttpStatus.OK); 
		}
		return new ResponseEntity<>("User not in course!", HttpStatus.NO_CONTENT); 
	}

}
