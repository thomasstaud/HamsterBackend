package at.ac.htlinn.hamsterbackend.courseManagement.solution.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.solution.model.Solution;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolutionDto {
	public SolutionDto(Solution solution) {
		this.id = solution.getId();
		this.activityId = solution.getActivity().getId();
		this.studentName = solution.getStudent().getUsername();
		this.code = solution.getCode();
		this.submitted = solution.isSubmitted();
		this.submissionDate = solution.getSubmissionDate();
		this.feedback = solution.getFeedback();
	}

	@JsonProperty("solution_id")
	private int id;
	@JsonProperty("activity_id")
	private int activityId;
	@JsonProperty("student_name")
	private String studentName;
	private String code;
	private boolean submitted;
	@JsonProperty("submission_date")
	private Date submissionDate;
	private String feedback;
}
