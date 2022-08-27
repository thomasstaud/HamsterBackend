package io.github.Hattinger04.course.model;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.course.CourseRepository;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.exercise.ExerciseRepository;
import io.github.Hattinger04.course.model.solution.Solution;
import io.github.Hattinger04.course.model.solution.SolutionRepository;
import io.github.Hattinger04.user.model.User;

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
		return courseRepository.save(course); 
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
	
	// TODO: working with student / teacher table 
	public void addStudentToCourse(Course course, User student) {
		
	}
	
	public void addStudentsToCourse(Course course, Set<User> student) {
	
	}
	
	public void removeStudentFromCourse(Course course, User student) {

	}
	
	public void removeStudentsFromCourse(Course course, Set<User> student) {

	}
	
	
	// wont work like that ofc
	public Exercise createExercise(Integer course_id, String name) {
		Exercise exercise = new Exercise(course_id, name);
		return exerciseRepository.save(exercise); 
	}
	
	public Exercise getExerciseByID(int id) {
		return exerciseRepository.findById(id); 
	}
	/**
	 * Get exercise by course_id and course name 
	 * 
	 * @param course_id
	 * @param name
	 * @return
	 */
	public Exercise getExerciseByCourse(int course_id, String name) {
		return exerciseRepository.findByCourse(course_id, name); 
	}
	
	public void deleteExercise(Integer course_id, String name) {
		exerciseRepository.delete(exerciseRepository.findByCourse(course_id, name));
	}
	
	
	public Solution createSolution(int exercise_id, String text) {
		Solution solution = new Solution(exercise_id, text); 
		return solutionRepository.save(solution); 
	}
	
	public Solution getSolutionByID(int id) {
		return solutionRepository.findById(id); 
	}
	/**
	 * Get solution by exercise_id and exercise name 
	 * 
	 * @param exercise_id
	 * @param name
	 * @return
	 */
	public Solution getSolutionByExercise(int exercise_id, String name) {
		return solutionRepository.findByExercise(exercise_id, name);
	}
	
	public void deleteSolution(int id) {
		solutionRepository.delete(solutionRepository.findById(id));
	}
	// TODO: teacher correcting students work 
}
