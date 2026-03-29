package at.ac.htlinn.hamsterbackend.courseManagement.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestResults {
	@JsonProperty("first_place")
	private ContestParticipant firstPlace;
	@JsonProperty("second_place")
	private ContestParticipant secondPlace;
	@JsonProperty("third_place")
	private ContestParticipant thirdPlace;
	@JsonProperty("user_place")
	private int userPlace;
	@JsonProperty("user_time")
	// time in seconds
	private long userTime;
}
