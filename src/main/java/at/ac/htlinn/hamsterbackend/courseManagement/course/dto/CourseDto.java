package at.ac.htlinn.hamsterbackend.courseManagement.course.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
	public CourseDto(Course course) {
		this.id = course.getId();
		this.name = course.getName();
		this.teacherId = course.getTeacher().getId();
	}

	private int id;
	private String name;
	@JsonProperty("teacher_id")
	private int teacherId;
}
