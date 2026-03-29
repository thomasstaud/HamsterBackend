package at.ac.htlinn.hamsterbackend.courseManagement.student.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import lombok.Data;

@Data
public class StudentCourseDto {
	public StudentCourseDto(Course course, List<StudentActivityDto> activityViews) {
		this.id = course.getId();
		this.courseName = course.getName();
		this.activityViews = activityViews;
	}
	
	@JsonProperty("course_id")
	private int id;
	@JsonProperty("course_name")
	private String courseName;
	@JsonProperty("activities")
	private List<StudentActivityDto> activityViews;
}
