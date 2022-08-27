package io.github.Hattinger04.course.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Entity
//@Table(name = "student")
@Data
@AllArgsConstructor
public class Student {

	private Integer id; // TODO: primary_key
	
	private Integer user_id; // TODO: FK
	
}
