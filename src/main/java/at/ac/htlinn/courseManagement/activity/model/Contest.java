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
	public Contest(ContestDTO contest, CourseService courseService) {
		super(contest.getId(), contest.getName(), contest.getDetails(),
				courseService.getCourseById(contest.getCourseId()));
		this.start = contest.getStart();
		this.hamsters = contest.getHamsters();
	}
	
	@Column(name = "start")
	private Date start;
	
	@Column(name = "hamsters", nullable = false)
	private String hamsters;
}
