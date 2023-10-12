package io.github.Hattinger04.course.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.Hattinger04.course.model.course.Course;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.exercise.ExerciseDTO;
import lombok.Data;

@Data
public class StudentViewDTO {
	public StudentViewDTO(Course course, List<Exercise> exercises) {
		this.courseId = course.getId();
		this.courseName = course.getName();
		this.exercises = new ArrayList<ExerciseDTO>();
		for (Exercise e : exercises) {
			ExerciseDTO exercise = new ExerciseDTO(e);
			this.exercises.add(exercise);
		}
	}
	
	@JsonProperty("course_id")
	private int courseId;
	@JsonProperty("course_name")
	private String courseName;
	@JsonProperty("exercises")
	private List<ExerciseDTO> exercises;
}
