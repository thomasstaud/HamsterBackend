package at.ac.htlinn.hamsterbackend.courseManagement.teacher.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Contest;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherActivityDto {
	public TeacherActivityDto(Activity activity, int submitted, int feedbacked) {
		this.id = activity.getId();
		this.name = activity.getName();
		this.details = activity.getDetails();
		this.hidden = activity.isHidden();
		
		if (activity instanceof Exercise) {
			date = ((Exercise)activity).getDeadline();
			type = Exercise.type;
		} else {
			date = ((Contest)activity).getStart();
			type = Contest.type;
		}
		
		this.submitted = submitted;
		this.feedbacked = feedbacked;
	}
	
	@JsonProperty("activity_id")
	private int id;
	private String name;
	private String details;
	private boolean hidden;
	
	// type specific fields
	// the meaning of date depends on activity type
	//		for exercises, it is the deadline
	//		for contests, it is the start date
	private Date date;
	private String type;
	
	// information about student submissions
	private int submitted;
	private int feedbacked;
}
