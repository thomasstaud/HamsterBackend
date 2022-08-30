package io.github.Hattinger04.course.model.student;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long>{
	Student findById(int id);
}
