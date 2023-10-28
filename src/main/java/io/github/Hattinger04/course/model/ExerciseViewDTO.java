package io.github.Hattinger04.course.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.course.model.solution.Solution;
import lombok.Data;

@Data
public class ExerciseViewDTO {
	public ExerciseViewDTO(Exercise exercise, Solution solution) {
		// exercise fields
		this.exerciseId = exercise.getId();
		this.name = exercise.getName();
		this.details = exercise.getDetails();
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
		
		// solution fields
		this.solutionId = solution.getId();
		this.code = solution.getCode();
		this.submitted = solution.isSubmitted();
		this.feedback = solution.getFeedback();
	}
	
	public ExerciseViewDTO(Exercise exercise) {
		this.exerciseId = exercise.getId();
		this.name = exercise.getName();
		this.details = exercise.getDetails();
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	// exercise fields
	@JsonProperty("exercise_id")
	private int exerciseId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("details")
	private String details;
	@JsonProperty("deadline")
	private Date deadline;
	@JsonProperty("hamster")
	private String hamster;
	
	// solution fields
	@JsonProperty("solution_id")
	private int solutionId;
	@JsonProperty("solution_code")
	private String code;
	@JsonProperty("submitted")
	private boolean submitted;
	@JsonProperty("feedback")
	private String feedback;
}
