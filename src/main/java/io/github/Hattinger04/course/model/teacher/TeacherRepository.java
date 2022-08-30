package io.github.Hattinger04.course.model.teacher;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long>{
	Teacher findById(int id); 
}
