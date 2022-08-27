package io.github.Hattinger04.course.model.course;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;

//@Entity
//@Table(name = "course")
@Data
@AllArgsConstructor
public class Course {

	public Course(String name) {
		this.name = name; 
	}
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "user_id")
	private Integer id; // TODO: primary_key
	
	private String name;  // TODO: unique
		
/*
	Not tested!
	Connection between course and user (n - m solution)
*/
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Set<Course> courses;

}
