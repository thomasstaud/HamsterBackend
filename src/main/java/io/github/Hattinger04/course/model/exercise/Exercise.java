package io.github.Hattinger04.course.model.exercise;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

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
@JsonTypeName("exercise") 
public class Exercise {

	public Exercise(Course course, String name, String details, Date deadline, String hamster) {
		this.course = course; 
		this.name = name;
		this.details = details;
		this.deadline = deadline;
		this.hamster = hamster;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exercise_id")
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "details")
	private String details;
	
	@Column(name = "deadline")
	private Date deadline;
	
	@Column(name = "hamster", nullable = false)
	private String hamster; 
	
	@ManyToOne
	@JoinColumn(name="course_id", nullable = false)
	private Course course;
}
