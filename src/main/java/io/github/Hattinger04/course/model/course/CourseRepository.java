package io.github.Hattinger04.course.model.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{

	Course findById(int id);
	Course findByName(String name); 
}
