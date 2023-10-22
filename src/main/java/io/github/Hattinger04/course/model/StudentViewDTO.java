package io.github.Hattinger04.course.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.Hattinger04.course.model.course.Course;
import lombok.Data;

@Data
public class StudentViewDTO {
	public StudentViewDTO(Course course, List<ExerciseViewDTO> exerciseViews) {
		this.courseId = course.getId();
		this.courseName = course.getName();
		this.exerciseViews = exerciseViews;
	}
	
	@JsonProperty("course_id")
	private int courseId;
	@JsonProperty("course_name")
	private String courseName;
	@JsonProperty("exercises")
	private List<ExerciseViewDTO> exerciseViews;
}
