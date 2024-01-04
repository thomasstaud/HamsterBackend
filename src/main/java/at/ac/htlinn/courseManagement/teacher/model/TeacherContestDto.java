package at.ac.htlinn.courseManagement.teacher.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.activity.model.Contest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherContestDto extends TeacherActivityDto {
	
	public TeacherContestDto(Contest contest) {
		super(contest.getId(), contest.getName(), contest.getDetails());
		this.start = contest.getStart();
		this.ignoreHamsterPosition = contest.isIgnoreHamsterPosition();
		this.visibleStartHamster = contest.getVisibleStartHamster();
		this.visibleEndHamster = contest.getVisibleEndHamster();
		this.hiddenStartHamster = contest.getHiddenStartHamster();
		this.hiddenEndHamster = contest.getHiddenStartHamster();
	}
	
	// used in the client to differentiate between the 2 activity types
	@JsonProperty("is_contest")
	private boolean isContest = true;
	
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
