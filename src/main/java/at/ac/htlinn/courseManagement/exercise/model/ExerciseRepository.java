package at.ac.htlinn.courseManagement.exercise.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>{
	Exercise findById(int id);
	
	@Query(value = "SELECT * FROM EXERCISE e where course_id=:course_id", nativeQuery = true)
	List<Exercise> findByCourseId(@Param("course_id") int course_id);
}
