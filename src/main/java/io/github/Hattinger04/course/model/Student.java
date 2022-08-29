package io.github.Hattinger04.course.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.github.Hattinger04.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "student")
@Data
@AllArgsConstructor
@ToString
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Integer id; // TODO: primary_key
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user; // TODO: FK
}
