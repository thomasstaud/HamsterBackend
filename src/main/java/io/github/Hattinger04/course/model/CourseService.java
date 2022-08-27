package io.github.Hattinger04.course.model;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.course.CourseRepository;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.exercise.ExerciseRepository;
import io.github.Hattinger04.course.model.solution.SolutionRepository;
import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserRepository;

public class CourseService {

	// TODO: Nothing(!) tested yet
	
	private CourseRepository courseRepository; 
	private ExerciseRepository exerciseRepository; 
	private SolutionRepository solutionRepository; 
	
	@Autowired
	public CourseService(CourseRepository courseRepository, ExerciseRepository exerciseRepository, SolutionRepository solutionRepository) {
		this.courseRepository = courseRepository;
		this.exerciseRepository = exerciseRepository;
		this.solutionRepository = solutionRepository;
	}

	
	public Course createCourse(String name) {
		Course course = new Course(name); 
		courseRepository.save(course); 
		return course; 
	}
	
	public void deleteCourse(String name) {
		courseRepository.delete(courseRepository.findByName(name));
	}
	
	public Course getCourseByID(int id) {
		return courseRepository.findById(id); 
	}
	
	public Course getCourseByName(String name) {
		return courseRepository.findByName(name); 
	}
	
	public void addStudentToCourse(Course course, User student) {
		
	}
	
	public void addStudentsToCourse(Course course, Set<User> student) {
	
	}
	
	public void removeStudentFromCourse(Course course, User student) {

	}
	
	public void removeStudentsFromCourse(Course course, Set<User> student) {

	}
	
	public Exercise getExerciseByID(int id) {
		return exerciseRepository.findById(id); 
	}
		
	// wont work like that ofc
	public Exercise createExercise(Integer course_id, String name) {
		Exercise exercise = new Exercise(course_id, name);
		exerciseRepository.save(exercise); 
		return exercise; 
	}
	
	public void deleteExercise(Integer course_id, String name) {
		exerciseRepository.delete(exerciseRepository.findByCourse(course_id, name));
	}
	
	// TODO: students submitting solution
	// TODO: teacher correcting students work 
}
