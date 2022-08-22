package io.github.Hattinger04.course;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@Entity
//@Table(name = "exercise")
@AllArgsConstructor
public class Exercise {

	@Getter @Setter
	private Integer id; // TODO: primary_key
	
	@Getter @Setter
	private String name; // TODO: unique 
	
	@Getter
	private String text; 
	
	public void setText(String text) {
		this.text = text;
		// TODO: if already published: publish changes to all students
	}
	
	@Getter @Setter
	private Integer course_id; 
	
	@Getter @Setter
	private Integer teacher_id; 
	
	@Getter @Setter
	private Integer student_id; 
	
/* TODO: best solution? 
 * @Getter @Setter
	private Boolean finished;
	
	@Getter @Setter
	private String answer; 
 
 */
}
