package at.ac.htlinn.hamsterbackend.courseManagement.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentExerciseDto extends StudentActivityDto {
	public StudentExerciseDto(Exercise exercise, StudentSolutionDto solution) {
		super(exercise.getId(), exercise.getName(), exercise.getDetails(),
				solution, exercise.getDeadline(), Exercise.type);
		this.terrainId = exercise.getTerrain().getTerrainId();
	}

	@JsonProperty("terrain_id")
	private long terrainId;
}
