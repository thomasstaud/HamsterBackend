package at.ac.htlinn.courseManagement.student.model;

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
}
