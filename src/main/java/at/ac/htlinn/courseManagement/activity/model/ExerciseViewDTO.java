package at.ac.htlinn.courseManagement.activity.model;

import java.util.Date;

import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.solution.model.SolutionViewDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExerciseViewDTO extends ActivityViewDTO{
	public ExerciseViewDTO(Exercise exercise, Solution solution) {
		super(exercise.getId(), exercise.getName(),
				exercise.getDetails(), new SolutionViewDTO(solution));
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	public ExerciseViewDTO(Exercise exercise) {
		super(exercise.getId(), exercise.getName(), exercise.getDetails(), null);
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	private Date deadline;
	private String hamster;
}
