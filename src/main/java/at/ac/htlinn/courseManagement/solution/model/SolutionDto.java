package at.ac.htlinn.courseManagement.solution.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolutionDto {
	public SolutionDto(Solution solution) {
		this.id = solution.getId();
		this.activityId = solution.getActivity().getId();
		this.studentId = solution.getStudent().getId();
		this.code = solution.getCode();
		this.submitted = solution.isSubmitted();
		this.submissionDate = solution.getSubmissionDate();
		this.feedback = solution.getFeedback();
	}

	@JsonProperty("solution_id")
	private int id;
	@JsonProperty("activity_id")
	private int activityId;
	@JsonProperty("student_id")
	private int studentId;
	private String code;
	private boolean submitted;
	@JsonProperty("submission-date")
	private Date submissionDate;
	private String feedback;
}
