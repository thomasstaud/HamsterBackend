package io.github.Hattinger04.course.model.exercise;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Entity
//@Table(name = "exercise")
@Data
@AllArgsConstructor
public class Exercise {

	public Exercise(Integer course_id, String name) {
		this.course_id = course_id; 
		this.name = name; 
	}
	
	private Integer id; // TODO: primary_key
	
	private String name; // TODO: unique 
	
	private String text; 
	
	private Integer course_id; // TODO: FK
}
