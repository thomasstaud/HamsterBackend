package at.ac.htlinn.courseManagement.exercise;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import at.ac.htlinn.courseManagement.exercise.model.Exercise;

@Service
public class ExerciseService {

	private ExerciseRepository exerciseRepository;
	
	public ExerciseService(ExerciseRepository exerciseRepository) {
		this.exerciseRepository = exerciseRepository;
	}
	
	
	
	public Exercise saveExercise(Exercise exercise) {
		return exerciseRepository.save(exercise);
	}

	public boolean deleteExercise(int exerciseId) {
		try {
			Exercise exercise = exerciseRepository.getById(exerciseId);
			exerciseRepository.delete(exercise);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public Exercise getExerciseById(int exerciseId) { 
		return exerciseRepository.getById(exerciseId);
	}

	public List<Exercise> getAllExercisesInCourse(int courseId) {
		
		// get exercises and convert to DTOs
		List<Exercise> exercises = new ArrayList<Exercise>();
		for(Exercise exercise : exerciseRepository.getByCourseId(courseId)) {
			exercises.add(exercise);
		}
		
		return exercises;
	}
}
