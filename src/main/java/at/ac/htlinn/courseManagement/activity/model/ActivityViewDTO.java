package at.ac.htlinn.courseManagement.activity.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.solution.model.SolutionViewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ActivityViewDTO {
	
	@JsonProperty("activity_id")
	private int id;
	private String name;
	private String details;
	@JsonProperty("solution")
	private SolutionViewDTO solutionView;
}
