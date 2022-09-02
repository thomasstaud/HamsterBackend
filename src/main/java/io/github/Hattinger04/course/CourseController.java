package io.github.Hattinger04.course;

import java.util.List;

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
import io.github.Hattinger04.course.model.teacher.Teacher;
import io.github.Hattinger04.user.model.User;

@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private RestServices restServices; 

	
	/**
	 * Get student by id in Course from database
	 * Needs course and student object 
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/getStudent")
	@ResponseBody
	public ResponseEntity<?> getStudent(@RequestBody String json) {
		Student s = (Student) restServices.deserialize(Student.class, json); 
		User student; 
		if((student = courseService.getUserByStudent(s)) == null) {
			return new ResponseEntity<>("Student not exisiting!", HttpStatus.NOT_FOUND);
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
		List<User> students; 
		if((students = courseService.getAllStudents(course)) == null) {
			return new ResponseEntity<>("Course not existing or no users in course!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(students, HttpStatus.OK); 
	}
		
	/**
	 * get teacher from course from database
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
		List<User> teachers; 
		if((teachers = courseService.getCourseTeachers(course)) == null) {
			return new ResponseEntity<>("There is no teacher in this course - contact an admin!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teachers, HttpStatus.OK); 
	}
	
	/**
	 * Create a new Course
	 * Needs course and teacher object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/createCourse")
	@ResponseBody
	public ResponseEntity<?> createCourse(@RequestBody String json) {
		Object[] objects; 
		Course course;
		if((objects = restServices.deserializeMany(new Class[] {Course.class, Teacher.class}, json)) != null) {
			if((course = courseService.createCourse((Course) objects[0])) != null) {
				courseService.setCourseTeacher(course, (Teacher) objects[1]); 
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>("Could not create course!", HttpStatus.NOT_IMPLEMENTED);
		}
		return new ResponseEntity<>("Wrong JSON format!", HttpStatus.NOT_IMPLEMENTED);
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
		return courseService.deleteCourse((Course) restServices.deserialize(Course.class, json)) ? 
				new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>("Could not delete course!", HttpStatus.NOT_MODIFIED);
	}

	/**
	 * Adds student to existing Course
	 * Needs course object + student object
	 * 
	 * @param json
	 * @return
	 */
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/addStudentCourse")
	@ResponseBody
	public ResponseEntity<?> addStudentCourse(@RequestBody String json) {
		Object[] objects = restServices.deserializeMany(new Class[] {Course.class, Student.class}, json); 
		return courseService.addStudentToCourse((Course) objects[0], (Student) objects[1]) ? 
				new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>("Could not add student to course!", HttpStatus.NOT_IMPLEMENTED); 
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
		Object[] objects = restServices.deserializeMany(new Class[] {Course.class, Student.class}, json); 
		return courseService.removeStudentFromCourse((Course) objects[0], (Student) objects[1]) ? 
				new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>("Could not remove student from course!", HttpStatus.NOT_MODIFIED); 
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
		return courseService.createExercise((Exercise) restServices.deserialize(Exercise.class, json)) != null ?
				new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>("Could not create exercise!", HttpStatus.NOT_IMPLEMENTED); 
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
		return courseService.createExercise((Exercise) restServices.deserialize(Exercise.class, json)) != null ?
				new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>("Could not patch exercise!", HttpStatus.NOT_IMPLEMENTED); 
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
		return courseService.deleteExercise((Exercise) restServices.deserialize(Exercise.class, json)) ?
				new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>("Could not delete exercise!", HttpStatus.NOT_IMPLEMENTED); 
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
