package at.ac.htlinn.hamsterbackend.courseManagement.activity.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Contest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContestDto extends ActivityDto {
	private final String type = Contest.type;
	
	public ContestDto(Contest contest) {
		super(contest.getId(), contest.getCourse().getId(),
				contest.getName(), contest.getDetails(), contest.isHidden());
		this.start = contest.getStart();
		this.ignoreHamsterPosition = contest.isIgnoreHamsterPosition();
		this.startTerrainId = contest.getStartTerrain().getTerrainId();
		this.endTerrainId = contest.getEndTerrain().getTerrainId();
		this.hiddenStartTerrainId = contest.getHiddenStartTerrain().getTerrainId();
		this.hiddenEndTerrainId = contest.getHiddenStartTerrain().getTerrainId();
	}

	private Date start;
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
