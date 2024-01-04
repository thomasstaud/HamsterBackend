package at.ac.htlinn.courseManagement.student.model;

import at.ac.htlinn.courseManagement.activity.model.Exercise;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentExerciseDto extends StudentActivityDto {
	public StudentExerciseDto(Exercise exercise, StudentSolutionDto solution) {
		super(exercise.getId(), exercise.getName(), exercise.getDetails(),
				solution, exercise.getDeadline(), Exercise.type);
		this.hamster = exercise.getHamster();
	}
	
	private String hamster;
}
