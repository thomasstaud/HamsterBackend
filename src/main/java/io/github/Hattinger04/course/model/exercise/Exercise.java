package io.github.Hattinger04.course.model.exercise;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import io.github.Hattinger04.course.model.course.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "exercise")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME, property="type")
@JsonTypeName("course") 

public class Exercise {

	public Exercise(Course course, String name) {
		this.course = course; 
		this.name = name; 
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exercise_id")
	private Integer id;
	
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "text", unique = false, nullable = true)
	private String text; 
	
	@ManyToOne
	@JoinColumn(name="course_id", nullable = false)
	private Course course;
}
