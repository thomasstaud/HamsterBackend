package at.ac.htlinn.hamsterbackend.courseManagement.student.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class StudentActivityDto {
	
	@JsonProperty("activity_id")
	private int id;
	private String name;
	private String details;
	@JsonProperty("solution")
	private StudentSolutionDto solutionView;
	
	// meaning of date depends on activity type
	//		for exercises, it is the deadline
	//		for contests, it is the start date
	private Date date;
	private String type;
}
