package at.ac.htlinn.hamsterbackend.courseManagement.activity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestParticipant {
	private String username;
	// time in seconds
	private long time; 
}
