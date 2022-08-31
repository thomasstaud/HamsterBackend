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
import io.github.Hattinger04.course.model.CourseService;
import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.student.Student;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private RestServices restServices; 

	
	/**
	 * Get student by id in Course from database
	 * Needs course object 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/getStudentByID")
	@ResponseBody
	public ResponseEntity<?> getStudentByID(@RequestBody String json) {
		Object[] objects = restServices.deserializeMany(new Class[] {Course.class, Student.class}, json); 
		Student student; 
		if((student = courseService.getStudent((Course)objects[0], (Student)objects[1])) == null) {
			return new ResponseEntity<>("Student not exisiting", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(student, HttpStatus.OK); 
	}
	
	/**
	 * Get all students in Course from database
	 * Needs course object 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/getAllStudents")
	@ResponseBody
	public ResponseEntity<?> getAllStudents(@RequestBody String json) {
		Course course = (Course) restServices.deserialize(Course.class, json);
		return new ResponseEntity<>(courseService.getAllStudents(course), HttpStatus.OK); 
	}
	
	/**
	 * Get all students in Course from database
	 * Needs course object 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/getCourseTeacher")
	@ResponseBody
	public ResponseEntity<?> getCourseTeacher(@RequestBody String json) {
		Course course = (Course) restServices.deserialize(Course.class, json);
		return new ResponseEntity<>(courseService.getCourseTeacher(course), HttpStatus.OK); 
	}
	
	/**
	 * Create a new Course
	 * Needs course object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/createCourse")
	@ResponseBody
	public ResponseEntity<?> createCourse(@RequestBody String json) {
		courseService.createCourse((Course) restServices.deserialize(Course.class, json));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * Deletes a existing Course
	 * Needs course object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@DeleteMapping("/deleteCourse")
	@ResponseBody
	public ResponseEntity<?> deleteCourse(@RequestBody String json) {
		courseService.deleteCourse((Course) restServices.deserialize(Course.class, json));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Adds student to existing Course
	 * Needs course object + student object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/addStudentCourse")
	@ResponseBody
	public ResponseEntity<?> addStudentCourse(@RequestBody String json) {
		// TODO: not sure if that will work - probably not
		Course course = (Course) restServices.deserialize(Course.class, json);
		Student student = (Student) restServices.deserialize(Student.class, json);
		courseService.addStudentToCourse(course, student); 
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Removes student from Course
	 * Needs course object + student object 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@DeleteMapping("/removeStudentCourse")
	@ResponseBody
	public ResponseEntity<?> removeStudentCourse(@RequestBody String json) {
		// TODO: not sure if that will work - probably not
		Course course = (Course) restServices.deserialize(Course.class, json);
		Student student = (Student) restServices.deserialize(Student.class, json);
		courseService.removeStudentFromCourse(course, student); 
		return new ResponseEntity<>(HttpStatus.OK); 
	}

	/**
	 * Creates a new exercise in a existing Course 
	 * Needs course object + exercise object 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/createExercise")
	@ResponseBody
	public ResponseEntity<?> createExercise(@RequestBody String json) {
		courseService.createExercise((Exercise) restServices.deserialize(Exercise.class, json));
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Changes already existing (and published) exercises
	 * Needs course object + exercise object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/patchExercise")
	@ResponseBody
	public ResponseEntity<?> patchExercise(@RequestBody String json) {
		courseService.createExercise((Exercise) restServices.deserialize(Exercise.class, json));
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Deletes existing exercise 
	 * Needs course object + exercise object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/deleteExercise")
	@ResponseBody
	public ResponseEntity<?> deleteExercise(@RequestBody String json) {
		courseService.deleteExercise((Exercise) restServices.deserialize(Exercise.class, json));
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	/**
	 * Gives a rating to an exercise for one student
	 * Needs course object + exercise object + student object 
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
