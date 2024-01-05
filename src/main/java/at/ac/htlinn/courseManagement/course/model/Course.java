package at.ac.htlinn.courseManagement.course.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.ac.htlinn.user.UserService;
import at.ac.htlinn.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeName("course") 
public class Course {
	public Course(CourseDto course, UserService userService) {
		this.id = course.getId();
		this.name = course.getName();
		this.teacher = userService.findUserByID(course.getTeacherId());
		// users must be added separately!
	}
	
	/* TODO: have a closer look at CascadeType
	*		if a course is deleted, the corresponding
	*		activities and user_course entries should be deleted along with it
	*
	*		CascadeType.ALL doesn't appear to work for this, even though it should
	*
	*		the same needs to be done for Activity and possibly other classes as well
	*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_id")
	private Integer id;
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name="teacher_id", nullable = false)
	private User teacher;
		
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Set<User> users;

}
