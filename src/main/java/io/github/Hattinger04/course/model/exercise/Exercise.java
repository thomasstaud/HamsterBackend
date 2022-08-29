package io.github.Hattinger04.course.model.exercise;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.github.Hattinger04.course.model.course.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "exercise")
@Data
@AllArgsConstructor
@ToString
public class Exercise {

	public Exercise(Course course, String name) {
		this.course = course; 
		this.name = name; 
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exercise_id")
	private Integer id; // TODO: primary_key
	
	@Column(name = "name", unique = true, nullable = false)
	private String name; // TODO: unique 
	
	@Column(name = "text", unique = false, nullable = true)
	private String text; 
	
	@ManyToOne
	@JoinColumn(name="course_id", nullable = false)
	private Course course; // TODO: FK
}
