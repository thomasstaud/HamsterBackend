package at.ac.htlinn.hamsterbackend.courseManagement.activity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ActivityDto {

	@JsonProperty("activity_id")
	private int id;
	@JsonProperty("course_id")
	private int courseId;
	private String name;
	private String details;
	private boolean hidden;
}
