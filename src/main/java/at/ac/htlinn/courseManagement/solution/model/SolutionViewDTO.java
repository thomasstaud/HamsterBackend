package at.ac.htlinn.courseManagement.solution.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SolutionViewDTO {
	public SolutionViewDTO(Solution solution) {
		this.solutionId = solution.getId();
		this.code = solution.getCode();
		this.submitted = solution.isSubmitted();
		this.submissionDate = solution.getSubmissionDate();
		this.feedback = solution.getFeedback();
	}
	
	@JsonProperty("solution_id")
	private int solutionId;
	private String code;
	private boolean submitted;
	private Date submissionDate;
	private String feedback;
}
