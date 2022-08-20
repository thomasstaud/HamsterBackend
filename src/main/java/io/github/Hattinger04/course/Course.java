package io.github.Hattinger04.course;

import java.util.List;

import io.github.Hattinger04.user.model.User;
import lombok.Getter;
import lombok.Setter;

public class Course {
	
	@Getter @Setter
	private int id; // TODO: unique 
	
	@Getter @Setter
	private String name;  // TODO: unique
	
	@Getter @Setter
	private List<User> teacher; // in case of multiple teacher in one group
	
	@Getter @Setter
	private List<User> students; 
	
	public Course(String name) {
		this.name = name; 
	}
	
}
