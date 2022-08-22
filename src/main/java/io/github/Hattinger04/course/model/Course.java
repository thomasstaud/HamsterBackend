package io.github.Hattinger04.course.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import io.github.Hattinger04.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "course")
@AllArgsConstructor
public class Course {
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "user_id")
	@Getter @Setter
	private Integer id; // TODO: primary_key
	
	@Getter @Setter
	private String name;  // TODO: unique
	
	@Getter @Setter
	private Set<User> teacher; // in case of multiple teacher in one group
	
	@Getter @Setter
	private Set<User> students; 
	
	public Course(String name) {
		this.name = name; 
	}
	
}
