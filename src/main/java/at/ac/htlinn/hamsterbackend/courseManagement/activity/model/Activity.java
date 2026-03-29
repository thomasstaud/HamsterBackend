package at.ac.htlinn.hamsterbackend.courseManagement.activity.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.InheritanceType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Activity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_id")
	private Integer id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "details")
	private String details;
	
	@Column(name = "hidden")
	private boolean hidden;
	
	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="course_id", nullable = false)
	private Course course;
}
