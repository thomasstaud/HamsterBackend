package io.github.Hattinger04.course.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Entity
//@Table(name = "teacher")
@Data
@AllArgsConstructor
public class Teacher {
	
	private Integer id; // TODO: primary_key
	
	private Integer user_id; // TODO: FK
	
}
