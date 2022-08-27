package io.github.Hattinger04.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.Hattinger04.RestServices;
import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;

@RestController
@RequestMapping("/teacher")
public class CourseController {

	@Autowired
	private UserService userService;
	@Autowired
	private RestServices restServices; 

	
	/**
	 * Get all students in Course from database
	 * Needs Course name 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/getAllStudents")
	@ResponseBody
	public ResponseEntity<?> getAllStudents(@RequestBody String json) {
		// TODO: adding to database Course table or smth like that
		// and return here all students of Course
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Create a new Course
	 * Needs Course name
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/createCourse")
	@ResponseBody
	public ResponseEntity<?> createCourse(@RequestBody String json) {
		// TODO: Create new Course with teacher and students
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * Deletes a existing Course
	 * Needs Course name
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@DeleteMapping("/deleteCourse")
	@ResponseBody
	public ResponseEntity<?> deleteCourse(@RequestBody String json) {
		// TODO: deleting existing Course and remove all database entries
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Adds student to existing Course
	 * Needs Course name + student name
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/addStudentCourse")
	@ResponseBody
	public ResponseEntity<?> addStudentCourse(@RequestBody String json) {
		// TODO: adding students to Course 
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Removes student from Course
	 * Needs Course name + student name 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@DeleteMapping("/removeStudentCourse")
	@ResponseBody
	public ResponseEntity<?> removeStudentCourse(@RequestBody String json) {
		// TODO: removing students from Course 
		return new ResponseEntity<>(HttpStatus.OK); 
	}

	/**
	 * Creates a new exercise in a existing Course 
	 * Needs Course name + exercise object 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/createExercise")
	@ResponseBody
	public ResponseEntity<?> createExercise(@RequestBody String json) {
		// TODO: create new exercise for Course 
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Changes already existing (and published) exercises
	 * Needs Course name + exercise object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/patchExercise")
	@ResponseBody
	public ResponseEntity<?> patchExercise(@RequestBody String json) {
		// TODO: change exercise for Course
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Deletes existing exercise 
	 * Needs Course name + exercise name
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/deleteExercise")
	@ResponseBody
	public ResponseEntity<?> deleteExercise(@RequestBody String json) {
		// TODO: delete existing exercise
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Gives a rating to an exercise for one student
	 * Needs Course name + exercise name + student name 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/rateExercise")
	@ResponseBody
	public ResponseEntity<?> rateExercise(@RequestBody String json) {
		// TODO: rate exercise from student 
		return new ResponseEntity<>(HttpStatus.OK); 
	}

}
