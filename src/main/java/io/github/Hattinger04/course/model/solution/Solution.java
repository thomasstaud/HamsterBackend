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

import io.github.Hattinger04.course.model.exercise.Exercise;
import io.github.Hattinger04.user.model.User;
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

	public Solution(Exercise exercise, User student, String code, String feedback) {
		this.exercise = exercise;
		this.student = student;
		this.code = code;
		this.feedback = feedback;
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
