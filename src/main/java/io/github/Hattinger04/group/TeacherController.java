package io.github.Hattinger04.group;

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
public class TeacherController {

	@Autowired
	private UserService userService;
	@Autowired
	private RestServices restServices; 

	
	/**
	 * Get all students in group from database
	 * Needs group name 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/getAllStudents")
	@ResponseBody
	public ResponseEntity<?> getAllStudents(@RequestBody String json) {
		// TODO: adding to database Group table or smth like that
		// and return here all students of Group
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Create a new group
	 * Needs group name
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/createGroup")
	@ResponseBody
	public ResponseEntity<?> createGroup(@RequestBody String json) {
		// TODO: Create new Group with teacher and students
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * Deletes a existing group
	 * Needs group name
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@DeleteMapping("/deleteGroup")
	@ResponseBody
	public ResponseEntity<?> deleteGroup(@RequestBody String json) {
		// TODO: deleting existing Group and remove all database entries
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Adds student to existing group
	 * Needs group name + student name
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/addStudentGroup")
	@ResponseBody
	public ResponseEntity<?> addStudentGroup(@RequestBody String json) {
		// TODO: adding students to Group 
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Removes student from group
	 * Needs group name + student name 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@DeleteMapping("/removeStudentGroup")
	@ResponseBody
	public ResponseEntity<?> removeStudentGroup(@RequestBody String json) {
		// TODO: removing students from Group 
		return new ResponseEntity<>(HttpStatus.OK); 
	}

	/**
	 * Creates a new exercise in a existing group 
	 * Needs group name + exercise object 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/createExercise")
	@ResponseBody
	public ResponseEntity<?> createExercise(@RequestBody String json) {
		// TODO: create new exercise for Group 
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Changes already existing (and published) exercises
	 * Needs group name + exercise object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/patchExercise")
	@ResponseBody
	public ResponseEntity<?> patchExercise(@RequestBody String json) {
		// TODO: change exercise for Group
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Deletes existing exercise 
	 * Needs group name + exercise name
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
	 * Needs group name + exercise name + student name 
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
