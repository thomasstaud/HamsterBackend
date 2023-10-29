package at.ac.htlinn.courseManagement.course.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.courseManagement.exercise.model.ExerciseViewDTO;
import lombok.Data;

@Data
public class CourseViewDTO {
	public CourseViewDTO(Course course, List<ExerciseViewDTO> exerciseViews) {
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
