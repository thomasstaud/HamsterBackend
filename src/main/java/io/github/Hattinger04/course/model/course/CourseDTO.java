package io.github.Hattinger04.course.model.course;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CourseDTO {
	public CourseDTO(Course course) {
		this.id = course.getId();
		this.name = course.getName();
	}

	@JsonProperty("id")
	private int id;
	@JsonProperty("name")
	private String name;
}
