package io.github.Hattinger04.course.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.exercise.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class StudentViewDTO {
	public StudentViewDTO(Course course, List<Exercise> exercises) {
		this.courseId = course.getId();
		this.courseName = course.getName();
		this.exercises = new ArrayList<ExerciseDTO>();
		for (Exercise e : exercises) {
			ExerciseDTO exercise = new ExerciseDTO(e.getId(), e.getName(), e.getHamster());
			this.exercises.add(exercise);
		}
	}
	
	@JsonProperty("course_id")
	private int courseId;
	@JsonProperty("course_name")
	private String courseName;
	@JsonProperty("exercises")
	private List<ExerciseDTO> exercises;

	@Data
	@AllArgsConstructor
	private class ExerciseDTO {
		@JsonProperty("exercise_id")
		private int exerciseId;
		@JsonProperty("exercise_name")
		private String exerciseName;
		@JsonProperty("hamster")
		private String hamster;
	}
}
