package io.github.Hattinger04.course.model.solution;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.github.Hattinger04.course.model.CourseService;
import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;
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
	public Solution(SolutionDTO solution, CourseService courseService, UserService userService) {
		this.id = solution.getId();
		this.exercise = courseService.getExerciseById(solution.getExerciseId());
		this.student = userService.findUserByID(solution.getStudentId());
		this.code = solution.getCode();
		this.submitted = solution.isSubmitted();
		// feedback must be set separately!
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "solution_id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="exercise_id", nullable = false)
	private Exercise exercise;
	
	@ManyToOne
	@JoinColumn(name="student_id", nullable = false)
	private User student;
	
	@Column(name = "code", nullable = false)	
	private String code;
	
	@Column(name = "submitted")
	private boolean submitted;
	
	@Column(name = "feedback")	
	private String feedback;
}
