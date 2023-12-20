package at.ac.htlinn.courseManagement.course.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.activity.model.ActivityViewDTO;
import lombok.Data;

@Data
public class CourseViewDTO {
	public CourseViewDTO(Course course, List<ActivityViewDTO> activityViews) {
		this.courseId = course.getId();
		this.courseName = course.getName();
		this.activityViews = activityViews;
	}
	
	@JsonProperty("course_id")
	private int courseId;
	@JsonProperty("course_name")
	private String courseName;
	@JsonProperty("exercises")
	private List<ActivityViewDTO> activityViews;
}
