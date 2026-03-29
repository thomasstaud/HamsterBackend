package at.ac.htlinn.hamsterbackend.courseManagement.teacher.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.user.dto.UserDto;
import lombok.Data;

@Data
public class TeacherCourseDto {
	public TeacherCourseDto(Course course, List<UserDto> students, List<TeacherActivityDto> activityViews) {
		this.id = course.getId();
		this.courseName = course.getName();
		this.students = students;
		this.activityViews = activityViews;
	}
	
	@JsonProperty("course_id")
	private int id;
	@JsonProperty("course_name")
	private String courseName;
	private List<UserDto> students;
	@JsonProperty("activities")
	private List<TeacherActivityDto> activityViews;
}
