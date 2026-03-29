package at.ac.htlinn.hamsterbackend.courseManagement.activity.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto extends ActivityDto {
	private final String type = Exercise.type;
	
	@Builder
	public ExerciseDto(int id, int courseId, String name, String details, boolean hidden,
			Date deadline, int terrainId) {
		super(id, courseId, name, details, hidden);
		this.deadline = deadline;
		this.terrainId = terrainId;
	}
	
	public ExerciseDto(Exercise exercise) {
		super(exercise.getId(), exercise.getCourse().getId(),
				exercise.getName(), exercise.getDetails(), exercise.isHidden());
		this.deadline = exercise.getDeadline();
		this.terrainId = exercise.getTerrain().getTerrainId();
	}

	private Date deadline;
	@JsonProperty("terrain_id")
	private long terrainId;
}
