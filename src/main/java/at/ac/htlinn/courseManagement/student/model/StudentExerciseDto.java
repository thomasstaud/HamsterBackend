package at.ac.htlinn.courseManagement.student.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.activity.model.Exercise;
import at.ac.htlinn.courseManagement.solution.model.Solution;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentExerciseDto extends StudentActivityDto {
	public StudentExerciseDto(Exercise exercise, Solution solution) {
		super(exercise.getId(), exercise.getName(),
				exercise.getDetails(), new StudentSolutionDto(solution));
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	public StudentExerciseDto(Exercise exercise) {
		super(exercise.getId(), exercise.getName(), exercise.getDetails(), null);
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	// used in the client to differentiate between activities
	@JsonProperty("is_contest")
	private boolean isContest = false;
	
	private Date deadline;
	private String hamster;
}
