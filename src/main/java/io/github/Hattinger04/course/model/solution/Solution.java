package io.github.Hattinger04.course.model.solution;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Entity
//@Table(name = "Solution")
@Data
@AllArgsConstructor
public class Solution {

	public Solution(Integer exercise_id, String text) {
		this.exercise_id = exercise_id; 
		this.text = text; 
	}
	
	private Integer id; // TODO: primary_key
		
	private String text; 
	
	private Integer exercise_id; // TODO: FK

}
