package io.github.Hattinger04.course.model;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.course.CourseRepository;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.exercise.ExerciseRepository;
import io.github.Hattinger04.course.model.solution.Solution;
import io.github.Hattinger04.course.model.solution.SolutionRepository;
import io.github.Hattinger04.user.model.User;

@Service
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

//	public Course createCourse(Course course) {
//		return courseRepository.save(course); 
//	}
//	public void deleteCourse(Course course) {
//		courseRepository.delete(course);
//	}
	
	public Course getCourseByID(int id) {
		return courseRepository.findById(id); 
	}
	
	public Course getCourseByName(String name) {
		return courseRepository.findByName(name); 
	}
	
	public List<Student> getAllStudents() {
		// TODO: Get all students
		return null;
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
	
	
//	public Exercise createExercise(Exercise exercise) {
//		return exerciseRepository.save(exercise); 
//	}
//	public void deleteExercise(Exercise exercise) {
//		exerciseRepository.delete(exercise);
//	}
	
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
	

//	
//	
//	public Solution createSolution(Solution solution) { 
//		return solutionRepository.save(solution); 
//	}
//	public void deleteSolution(Solution solution) {
//		solutionRepository.delete(solution);
//	}

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
	
	// TODO: teacher correcting students work 
}
