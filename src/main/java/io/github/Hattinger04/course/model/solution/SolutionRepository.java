package io.github.Hattinger04.course.model.solution;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SolutionRepository extends JpaRepository<Solution, Long>{
	Solution findById(int id);
	
	@Query(value = "SELECT * FROM solution s WHERE exercise_id=:exercise_id", nativeQuery = true)
	List<Solution> findByExerciseId(@Param("exercise_id")int exercise_id);
	
	@Query(value = "SELECT * FROM solution s JOIN exercise e USING (exercise_id)"
			+ "WHERE student_id=:student_id AND course_id=:course_id", nativeQuery = true)
	List<Solution> findByStudentIdInSpecifiedCourse(@Param("student_id")int student_id, @Param("course_id")int course_id);

	@Query(value = "SELECT * FROM solution s WHERE exercise_id=:exercise_id AND student_id=:student_id", nativeQuery = true)
	Solution findByExerciseAndStudentId(@Param("exercise_id")int exercise_id, @Param("student_id")int student_id);
}
