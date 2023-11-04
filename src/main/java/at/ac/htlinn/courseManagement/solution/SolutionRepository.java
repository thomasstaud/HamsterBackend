package at.ac.htlinn.courseManagement.solution;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.htlinn.courseManagement.solution.model.Solution;

public interface SolutionRepository extends JpaRepository<Solution, Integer>{
	
	@Query(value = "SELECT s.* FROM solution s WHERE exercise_id=:exercise_id", nativeQuery = true)
	public List<Solution> getByExerciseId(@Param("exercise_id") int exerciseId);
	
	@Query(value = "SELECT s.* FROM solution s JOIN exercise e USING (exercise_id)"
			+ "WHERE student_id=:student_id AND course_id=:course_id", nativeQuery = true)
	public List<Solution> getByStudentIdInSpecifiedCourse(@Param("student_id") int studentId, @Param("course_id") int courseId);

	@Query(value = "SELECT s.* FROM solution s WHERE exercise_id=:exercise_id AND student_id=:student_id", nativeQuery = true)
	public Solution getByExerciseAndStudentId(@Param("exercise_id") int exerciseId, @Param("student_id") int studentId);
}
