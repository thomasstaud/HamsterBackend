package io.github.Hattinger04.course.model;

import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "teacher")
@AllArgsConstructor
public class Teacher {
	
	@Getter @Setter
	private Integer id; // TODO: primary_key
	
	@Getter @Setter
	private String name; // TODO: unique
	
}
