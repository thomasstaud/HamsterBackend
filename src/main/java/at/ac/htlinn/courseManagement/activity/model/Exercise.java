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
@Table(name = "exercise")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeName("exercise") 
public class Exercise extends Activity {
	public Exercise(ExerciseDto exercise, CourseService courseService) {
		super(exercise.getId(), exercise.getName(), exercise.getDetails(),
				courseService.getCourseById(exercise.getCourseId()));
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	@Column(name = "deadline")
	private Date deadline;
	
	@Column(name = "hamster", nullable = false)
	private String hamster;
}
