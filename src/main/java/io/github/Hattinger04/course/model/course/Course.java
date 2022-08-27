package io.github.Hattinger04.course.model.course;

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

	public Course(String name) {
		this.name = name; 
	}
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "user_id")
	@Getter @Setter
	private Integer id; // TODO: primary_key
	
	@Getter @Setter
	private String name;  // TODO: unique
		
/*
	Not tested!
	Connection between course and user (n - m solution)
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Set<Course> courses;
*/
}
