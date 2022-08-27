package io.github.Hattinger04.course.model.solution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "Solution")
@AllArgsConstructor
public class Solution {

	public Solution(Integer exercise_id, String text) {
		this.exercise_id = exercise_id; 
		this.text = text; 
	}
	
	@Getter @Setter
	private Integer id; // TODO: primary_key
		
	@Getter @Setter
	private String text; 
	
	@Getter @Setter
	private Integer exercise_id; // TODO: FK

}
