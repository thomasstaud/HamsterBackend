package io.github.Hattinger04.course;

import javax.persistence.Entity;
import javax.persistence.Table;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "student")
@AllArgsConstructor
public class Student {

	@Getter @Setter
	private int id; // TODO: primary_key
	
	@Getter @Setter
	private String name; // TODO: unique
	
}
