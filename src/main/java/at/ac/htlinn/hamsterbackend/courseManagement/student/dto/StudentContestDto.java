package at.ac.htlinn.hamsterbackend.courseManagement.student.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Contest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentContestDto extends StudentActivityDto {
	public StudentContestDto(Contest contest, StudentSolutionDto solution) {
		super(contest.getId(), contest.getName(), contest.getDetails(),
				solution, contest.getStart(), Contest.type);
		this.ignoreHamsterPosition = contest.isIgnoreHamsterPosition();
		this.startTerrainId = contest.getStartTerrain().getTerrainId();
		this.endTerrainId = contest.getEndTerrain().getTerrainId();
		this.hiddenStartTerrainId = contest.getHiddenStartTerrain().getTerrainId();
		this.hiddenEndTerrainId = contest.getHiddenStartTerrain().getTerrainId();
	}
	
	@JsonProperty("ignore_hamster_position")
	private boolean ignoreHamsterPosition;

	@JsonProperty("start_terrain_id")
	private long startTerrainId;
	@JsonProperty("end_terrain_id")
	private long endTerrainId;

	@JsonProperty("hidden_start_terrain_id")
	private long hiddenStartTerrainId;
	@JsonProperty("hidden_end_terrain_id")
	private long hiddenEndTerrainId;
}
