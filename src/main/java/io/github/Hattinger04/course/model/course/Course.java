package io.github.Hattinger04.course.model.course;

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

import io.github.Hattinger04.user.model.User;
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

	public Course(String name, User teacher) {
		this.name = name; 
		this.teacher = teacher;
	}
	
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
