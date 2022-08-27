package io.github.Hattinger04.course.model.exercise;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "exercise")
@AllArgsConstructor
public class Exercise {

	public Exercise(Integer course_id, String name) {
		this.course_id = course_id; 
		this.name = name; 
	}
	
	@Getter @Setter
	private Integer id; // TODO: primary_key
	
	@Getter @Setter
	private String name; // TODO: unique 
	
	@Getter @Setter
	private String text; 
	
	@Getter @Setter
	private Integer course_id; // TODO: FK

}
