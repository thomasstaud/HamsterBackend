package at.ac.htlinn.courseManagement.activity.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class ActivityDTO {

	@JsonProperty("activity_id")
	private int id;
	@JsonProperty("course_id")
	private int courseId;
	private String name;
	private String details;
}
