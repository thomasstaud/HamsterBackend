package at.ac.htlinn.courseManagement.activity.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto extends ActivityDto {
	public ExerciseDto(Exercise exercise) {
		super(exercise.getId(), exercise.getCourse().getId(),
				exercise.getName(), exercise.getDetails(), exercise.isHidden());
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}

	private Date deadline;
	private String hamster;
}
