package at.ac.htlinn.courseManagement.activity.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import at.ac.htlinn.courseManagement.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorColumn(name="activity_type")
@Table(name = "activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Activity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exercise_id")
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "details")
	private String details;
	
	@ManyToOne
	@JoinColumn(name="course_id", nullable = false)
	private Course course;
}
