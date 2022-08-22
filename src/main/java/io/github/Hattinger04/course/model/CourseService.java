package io.github.Hattinger04.course.model;

import java.util.Set;

import io.github.Hattinger04.user.model.User;

public class CourseService {

	public Course createCourse(String name) {
		// TODO: implementation in db
		return new Course(name); 
	}
	
	public void deleteCourse(String name) {
		// TODO: implementation in db 
	}
	
	public Course getCourseByID(int id) {
		// TODO: get course by id
		return null; 
	}
	
	public Course getCourseByName(String name) {
		// TODO: get course by name
		return null; 
	}
	
	public void addStudentToCourse(Course course, User student) {
		course.getStudents().add(student); // Not sure if this works! - implementation in db!
	}
	
	public void addStudentsToCourse(Course course, Set<User> student) {
		course.getStudents().addAll(student); // Not sure if this works! - implementation in db!
	}
	
	public void removeStudentFromCourse(Course course, User student) {
		course.getStudents().remove(student); // Not sure if this works! - implementation in db!

	}
	
	public void removeStudentsFromCourse(Course course, Set<User> student) {
		course.getStudents().removeAll(student); // Not sure if this works! - implementation in db!
	}
	
	// wont work like that ofc
	public Exercise createExercise(String name) {
		// TODO: implementation in db
		return new Exercise(name); 
	}
	
	public void deleteExercise(String name) {
		// TODO: implementation in db
	}
	
	// TODO: students submitting solution
	// TODO: teacher correcting students work 
}
