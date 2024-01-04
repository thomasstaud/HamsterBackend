package at.ac.htlinn.courseManagement.student.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.activity.model.Contest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentContestDto extends StudentActivityDto {
	public StudentContestDto(Contest contest, StudentSolutionDto solution) {
		super(contest.getId(), contest.getName(), contest.getDetails(),
				solution, contest.getStart(), Contest.type);
		this.ignoreHamsterPosition = contest.isIgnoreHamsterPosition();
		this.visibleStartHamster = contest.getVisibleStartHamster();
		this.visibleEndHamster = contest.getVisibleEndHamster();
		this.hiddenStartHamster = contest.getHiddenStartHamster();
		this.hiddenEndHamster = contest.getHiddenStartHamster();
	}
	
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
