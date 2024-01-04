package at.ac.htlinn.courseManagement.teacher.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.activity.model.Exercise;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherExerciseDto extends TeacherActivityDto {
	
	public TeacherExerciseDto(Exercise exercise) {
		super(exercise.getId(), exercise.getName(), exercise.getDetails());
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	// used in the client to differentiate between activities
	@JsonProperty("is_contest")
	private boolean isContest = false;
	
	private Date deadline;
	private String hamster;
}
