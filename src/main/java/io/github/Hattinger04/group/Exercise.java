package io.github.Hattinger04.group;

import lombok.Getter;
import lombok.Setter;

public class Exercise {

	@Getter @Setter
	private int id; // TODO: unique
	
	@Getter @Setter
	private String name; // TODO: unique 
	
	@Getter
	private String text; 
	
	public void setText(String text) {
		this.text = text;
		// TODO: if already published: publish changes to all students
	}
	
	// does exercise refer to student or group? 
}
