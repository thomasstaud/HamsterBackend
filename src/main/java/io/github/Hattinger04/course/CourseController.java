package io.github.Hattinger04.course;

import java.util.List;

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
	 * Get student by id from database Needs as @PathVariable student_id
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
	 * Get student by id from database Needs as @PathVariable student_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/students/{id}")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getStudentByID(@PathVariable int id) {
		Student student = courseService.getStudentByID(id); 
		if (student == null || courseService.getUserByStudent(student) == null) {
			return new ResponseEntity<>("Student not exisiting!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
		

	/**
	 * Get student from course by id from database 
	 * Needs as @PathVariable student_id and courseid
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/{courseid}/students/{studentsid}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getStudentInCourseByCourseID(@PathVariable int courseid, @PathVariable int studentid) {
		Student student = courseService.getStudentByID(studentid); 
		Course course = courseService.getCourseByID(studentid); 
		User user = courseService.getUserByStudent(student); 
		if (student == null || user == null || courseService.getUserByStudent(student) == null || courseService.isUserInCourse(course, user)) {
			return new ResponseEntity<>("Student not exisiting!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	
	/**
	 * Get all students in Course from database Needs as @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/{id}/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseID(@PathVariable int id) {
		Course course = courseService.getCourseByID(id); 
		List<User> students;
		if ((students = courseService.getAllStudentsInCourse(course)) == null) {
			return new ResponseEntity<>("Course not existing or no users in course!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
	/**
	 * Get all students in Course from database Needs as @RequestParam coursename
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseName(@RequestParam(name = "coursename", required = false) String coursename) {
		Course course = courseService.getCourseByName(coursename); 
		List<User> students;
		if ((students = courseService.getAllStudentsInCourse(course)) == null) {
			return new ResponseEntity<>("Course not existing or no users in course!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(students, HttpStatus.OK);
	}

	/**
	 * Adds student to existing Course Needs course object + student object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/course/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Student student = mapper.convertValue(node.get("student"), Student.class);
		return courseService.addStudentToCourse(course, student) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not add student to course!", HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Removes student from Course Needs course object + student object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/course/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> removeStudentCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Student student = mapper.convertValue(node.get("student"), Student.class);
		return courseService.removeStudentFromCourse(course, student) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not remove student from course!", HttpStatus.NOT_MODIFIED);
	}
	
	/**
	 * Get teacher from course from database Needs as @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/{id}/teachers")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseTeacherByCourseID(@PathVariable int id) {
		Course course = courseService.getCourseByID(id); 
		List<User> teachers;
		if ((teachers = courseService.getCourseTeachers(course)) == null) {
			return new ResponseEntity<>("There is no teacher in this course - contact an admin!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	
	/**
	 * get teacher from course from database Needs as @RequestParam coursename
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/teachers")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseTeacherByCourseName(@RequestParam(name = "coursename", required = false) String coursename) {
		Course course = courseService.getCourseByName(coursename); 
		List<User> teachers;
		if ((teachers = courseService.getCourseTeachers(course)) == null) {
			return new ResponseEntity<>("There is no teacher in this course - contact an admin!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	/**
	 * Get course from database Needs as @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/{id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseByID(@PathVariable int id) {
		Course course = courseService.getCourseByID(id); 
		if (course == null) {
			return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	
	/**
	 * Returns all or one course
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseByCoursename(@RequestParam(name = "coursename", required = false) String coursename) {
		if(coursename == null) {
			return new ResponseEntity<>(courseService.getAllCourses(), HttpStatus.OK);
		}
		Course course = courseService.getCourseByName(coursename); 
		if(course == null) {
			return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
	}
	
	/**
	 * Create a new Course Needs course and teacher object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/course")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Teacher teacher = mapper.convertValue(node.get("teacher"), Teacher.class);
		if (courseService.createCourse(course) != null) {
			courseService.setCourseTeacher(course, teacher);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>("Could not create course!", HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Deletes a existing Course Needs course object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/course")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		return courseService.deleteCourse(course) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete course!", HttpStatus.NOT_MODIFIED);
	}



	/**
	 * Creates a new exercise in a existing Course Needs course object + exercise
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
				: new ResponseEntity<>("Could not create exercise!", HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Changes already existing (and published) exercises Needs course object +
	 * exercise object
	 * 
	 * @param json
	 * @return
	 */
	@PutMapping("/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> patchExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exericse"), Exercise.class);
		return courseService.createExercise(exercise) != null ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not patch exercise!", HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Deletes existing exercise Needs course object + exercise object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/deleteExercise")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		return courseService.deleteExercise(exercise) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.NOT_IMPLEMENTED);
	}

	/**
	 * Gives a rating to an exercise for one student Needs course object + exercise
	 * object + student object
	 * 
	 * @param json
	 * @return
	 */
	// TODO: change url!
	@PostMapping("/rateExercise")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> rateExercise(@RequestBody JsonNode node) {
		// TODO: rate exercise from student
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// TODO: change url!
	// Not tested!
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
			return new ResponseEntity<>("Course not found!", HttpStatus.NOT_FOUND); 
		}
		User user = mapper.convertValue(node.get("user"), User.class); 
		if(courseService.isUserInCourse(course, user)) {
			return new ResponseEntity<>(HttpStatus.OK); 
		}
		return new ResponseEntity<>("User not in course!", HttpStatus.NOT_FOUND); 
	}

}
