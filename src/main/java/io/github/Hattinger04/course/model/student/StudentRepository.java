package io.github.Hattinger04.course.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Long>{
	Student findById(int id);
	@Query(value = "SELECT * FROM STUDENT s JOIN user u USING (user_id) where u.username=:username", nativeQuery = true)
	Student findByName(@Param("username") String username); 
}
