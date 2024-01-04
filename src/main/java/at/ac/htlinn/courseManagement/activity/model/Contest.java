package at.ac.htlinn.courseManagement.activity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.ac.htlinn.courseManagement.course.CourseService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "contest")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeName("contest") 
public class Contest extends Activity {
	public static final String type = "contest";
	
	public Contest(ContestDto contest, CourseService courseService) {
		super(contest.getId(), contest.getName(), contest.getDetails(), contest.isHidden(),
				courseService.getCourseById(contest.getCourseId()));
		this.start = contest.getStart();
		this.visibleStartHamster = contest.getVisibleStartHamster();
		this.visibleEndHamster = contest.getVisibleEndHamster();
		this.hiddenStartHamster = contest.getHiddenStartHamster();
		this.hiddenEndHamster = contest.getHiddenStartHamster();
	}
	
	@Column(name = "start")
	private Date start;
	@Column(name = "ignore_hamster_position")
	private boolean ignoreHamsterPosition;
	
	// initial territory; visible for students
	@Column(name = "visible_start_hamster", nullable = false)
	private String visibleStartHamster;
	// expected resulting territory; visible for students (optional)
	@Column(name = "visible_end_hamster")
	private String visibleEndHamster;
	
	// initial territory; hidden for students (optional)
	@Column(name = "hidden_start_hamster")
	private String hiddenStartHamster;
	// expected resulting territory; hidden for students (optional)
	@Column(name = "hidden_end_hamster")
	private String hiddenEndHamster;
}
