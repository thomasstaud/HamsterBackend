package io.github.Hattinger04.course.model.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>{
	Exercise findById(int id);
	
	// TODO: SQL not tested yet!
	@Query(value = "SELECT * FROM EXERCISE e JOIN course c USING (course_id) where course_id=:course_id and c.name=:name", nativeQuery = true)
	Exercise findByCourse(@Param("course_id") long course_id, @Param("name")String name);
}
