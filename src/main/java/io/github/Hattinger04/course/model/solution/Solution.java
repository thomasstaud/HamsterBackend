package io.github.Hattinger04.course.model.solution;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.github.Hattinger04.course.model.exercise.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "solution")
@Data
@AllArgsConstructor
@ToString
public class Solution {

	public Solution(Exercise exercise, String text) {
		this.exercise = exercise; 
		this.text = text; 
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "solution_id")
	private Integer id; // TODO: primary_key
	
	@Column(name = "text", unique = false, nullable = true)	
	private String text; 
	
	@ManyToOne
	@JoinColumn(name="exercise_id", nullable = false)
	private Exercise exercise; // TODO: FK

}
