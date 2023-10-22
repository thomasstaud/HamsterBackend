package io.github.Hattinger04.course.model.solution;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SolutionDTO {
	public SolutionDTO(Solution solution) {
		this.id = solution.getId();
		this.exerciseId = solution.getExercise().getId();
		this.studentId = solution.getStudent().getId();
		this.code = solution.getCode();
		this.submitted = solution.isSubmitted();
		this.feedback = solution.getFeedback();
	}

	@JsonProperty("id")
	private int id;
	@JsonProperty("exercise_id")
	private int exerciseId;
	@JsonProperty("student_id")
	private int studentId;
	@JsonProperty("code")
	private String code;
	@JsonProperty("submitted")
	private boolean submitted;
	@JsonProperty("feedback")
	private String feedback;
}
