package at.ac.htlinn.courseManagement.solution.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.ac.htlinn.courseManagement.activity.ActivityService;
import at.ac.htlinn.courseManagement.activity.model.Activity;
import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "solution")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeName("solution") 
public class Solution {
	public Solution(SolutionDTO solution, ActivityService exerciseService, UserService userService) {
		this.id = solution.getId();
		this.activity = exerciseService.getActivityById(solution.getActivityId());
		this.student = userService.findUserByID(solution.getStudentId());
		this.code = solution.getCode();
		this.submitted = solution.isSubmitted();
		this.submissionDate = solution.getSubmissionDate();
		// feedback must be set separately!
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "solution_id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="activity_id", nullable = false)
	private Activity activity;
	
	@ManyToOne
	@JoinColumn(name="student_id", nullable = false)
	private User student;
	
	@Column(name = "code", nullable = false)	
	private String code;
	
	@Column(name = "submitted")
	private boolean submitted;

	@Column(name = "submission_date")
	private Date submissionDate;
	
	@Column(name = "feedback")	
	private String feedback;
}
