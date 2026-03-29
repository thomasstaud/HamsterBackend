package at.ac.htlinn.hamsterbackend.courseManagement.solution;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.htlinn.hamsterbackend.courseManagement.solution.model.Solution;

public interface SolutionRepository extends JpaRepository<Solution, Integer>{
	
	@Query(value = "SELECT s.* FROM solution s WHERE activity_id=:activity_id", nativeQuery = true)
	public List<Solution> getByActivityId(@Param("activity_id") int activityId);
	
	@Query(value = "SELECT s.* FROM solution s JOIN activity e USING (activity_id)"
			+ "WHERE student_id=:student_id AND course_id=:course_id", nativeQuery = true)
	public List<Solution> getByStudentIdInSpecifiedCourse(@Param("student_id") int studentId, @Param("course_id") int courseId);

	@Query(value = "SELECT s.* FROM solution s WHERE activity_id=:activity_id AND student_id=:student_id", nativeQuery = true)
	public Solution getByActivityAndStudentId(@Param("activity_id") int activityId, @Param("student_id") int studentId);
	
	@Query(value = "SELECT COUNT(*) FROM solution WHERE activity_id=:activity_id AND submitted = 1", nativeQuery = true)
	public int getNumberOfSubmittedSolutions(@Param("activity_id") int activityId);
	
	@Query(value = "SELECT COUNT(*) FROM solution WHERE activity_id=:activity_id AND feedback IS NOT NULL", nativeQuery = true)
	public int getNumberOfFeedbackedSolutions(@Param("activity_id") int activityId);
}
