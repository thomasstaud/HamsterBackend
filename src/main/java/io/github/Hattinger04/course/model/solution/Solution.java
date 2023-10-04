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
import io.github.Hattinger04.course.model.student.Student;
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

	public Solution(Exercise exercise, Student student, String hamster) {
		this.exercise = exercise;
		this.student = student;
		this.hamster = hamster;
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
	private Student student;
	
	@Column(name = "hamster", unique = false, nullable = false)	
	private String hamster;
}
