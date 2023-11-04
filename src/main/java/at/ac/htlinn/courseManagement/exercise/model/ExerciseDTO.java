package at.ac.htlinn.courseManagement.exercise.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExerciseDTO {
	public ExerciseDTO(Exercise exercise) {
		this.id = exercise.getId();
		this.courseId = exercise.getCourse().getId();
		this.name = exercise.getName();
		this.details = exercise.getDetails();
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}

	@JsonProperty("exercise_id")
	private int id;
	@JsonProperty("course_id")
	private int courseId;
	private String name;
	private String details;
	private Date deadline;
	private String hamster;
}
