package io.github.Hattinger04.course.model.solution;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import io.github.Hattinger04.course.model.exercise.Exercise;
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
@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME, property="type")
@JsonTypeName("solution") 
public class Solution {

	public Solution(Exercise exercise, String hamster) {
		this.exercise = exercise; 
		this.hamster = hamster; 
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "solution_id")
	private Integer id;
	
	@Column(name = "hamster", unique = false, nullable = false)	
	private String hamster; 
	
	@ManyToOne
	@JoinColumn(name="exercise_id", nullable = false)
	private Exercise exercise; 

}
