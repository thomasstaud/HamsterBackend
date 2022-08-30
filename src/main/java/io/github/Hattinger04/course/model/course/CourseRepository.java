package io.github.Hattinger04.course.model.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.Hattinger04.course.model.student.Student;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
	Course findById(int id);
	Course findByName(String name);
	// TODO: SQL not tested yet!
	@Query(value = "SELECT * FROM STUDENTS s JOIN USER u using(user_id) JOIN course c using(course_id, user_id) where c.course_id=:course_id", nativeQuery = true)
	List<Student> getAllStudents(int course_id); 
}
