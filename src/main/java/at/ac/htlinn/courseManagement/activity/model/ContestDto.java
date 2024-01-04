package at.ac.htlinn.courseManagement.activity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContestDto extends ActivityDto {
	public ContestDto(Contest contest) {
		super(contest.getId(), contest.getCourse().getId(),
				contest.getName(), contest.getDetails(), contest.isHidden());
		this.start = contest.getStart();
		this.ignoreHamsterPosition = contest.isIgnoreHamsterPosition();
		this.visibleStartHamster = contest.getVisibleStartHamster();
		this.visibleEndHamster = contest.getVisibleEndHamster();
		this.hiddenStartHamster = contest.getHiddenStartHamster();
		this.hiddenEndHamster = contest.getHiddenStartHamster();
	}

	private Date start;
	@JsonProperty("ignore_hamster_position")
	private boolean ignoreHamsterPosition;

	@JsonProperty("visible_start_hamster")
	private String visibleStartHamster;
	@JsonProperty("visible_end_hamster")
	private String visibleEndHamster;

	@JsonProperty("hidden_start_hamster")
	private String hiddenStartHamster;
	@JsonProperty("hidden_end_hamster")
	private String hiddenEndHamster;
}
