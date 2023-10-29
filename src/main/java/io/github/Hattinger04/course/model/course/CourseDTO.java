package io.github.Hattinger04.course.model.course;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseDTO {
	public CourseDTO(Course course) {
		this.id = course.getId();
		this.name = course.getName();
		this.teacherId = course.getTeacher().getId();
	}

	private int id;
	private String name;
	@JsonProperty("teacher_id")
	private int teacherId;
}
