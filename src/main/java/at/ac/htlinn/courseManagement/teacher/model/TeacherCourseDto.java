package at.ac.htlinn.courseManagement.teacher.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.course.model.Course;
import at.ac.htlinn.user.model.UserDto;
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
