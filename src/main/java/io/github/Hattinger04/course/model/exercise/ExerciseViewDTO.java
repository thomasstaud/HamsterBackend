package io.github.Hattinger04.course.model.exercise;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.Hattinger04.course.model.solution.Solution;
import io.github.Hattinger04.course.model.solution.SolutionViewDTO;
import lombok.Data;

@Data
public class ExerciseViewDTO {
	public ExerciseViewDTO(Exercise exercise, Solution solution) {
		this.exerciseId = exercise.getId();
		this.name = exercise.getName();
		this.details = exercise.getDetails();
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
		this.solutionView = new SolutionViewDTO(solution);
	}
	
	public ExerciseViewDTO(Exercise exercise) {
		this.exerciseId = exercise.getId();
		this.name = exercise.getName();
		this.details = exercise.getDetails();
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	@JsonProperty("exercise_id")
	private int exerciseId;
	private String name;
	private String details;
	private Date deadline;
	private String hamster;
	@JsonProperty("solution")
	private SolutionViewDTO solutionView;
}
