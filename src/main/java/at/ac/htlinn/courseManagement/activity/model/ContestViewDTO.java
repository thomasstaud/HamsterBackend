package at.ac.htlinn.courseManagement.activity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.solution.model.Solution;
import at.ac.htlinn.courseManagement.solution.model.SolutionViewDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContestViewDTO extends ActivityViewDTO {
	public ContestViewDTO(Contest contest, Solution solution) {
		super(contest.getId(), contest.getName(),
				contest.getDetails(), new SolutionViewDTO(solution));	
		this.start = contest.getStart();
		this.ignoreHamsterPosition = contest.isIgnoreHamsterPosition();
		this.visibleStartHamster = contest.getVisibleStartHamster();
		this.visibleEndHamster = contest.getVisibleEndHamster();
		this.hiddenStartHamster = contest.getHiddenStartHamster();
		this.hiddenEndHamster = contest.getHiddenStartHamster();
	}
	
	public ContestViewDTO(Contest contest) {
		super(contest.getId(), contest.getName(), contest.getDetails(), null);
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
