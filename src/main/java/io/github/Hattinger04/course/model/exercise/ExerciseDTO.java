package io.github.Hattinger04.course.model.exercise;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExerciseDTO {
	public ExerciseDTO(Exercise exercise) {
		this.id = exercise.getId();
		this.courseId = exercise.getCourse().getId();
		this.name = exercise.getName();
		this.details = exercise.getDetails();
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	@JsonProperty("id")
	private int id;
	@JsonProperty("course_id")
	private int courseId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("details")
	private String details;
	@JsonProperty("deadline")
	private Date deadline;
	@JsonProperty("hamster")
	private String hamster;
}
