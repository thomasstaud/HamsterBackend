package at.ac.htlinn.courseManagement.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.htlinn.courseManagement.course.model.Course;

public interface CourseRepository extends JpaRepository<Course, Integer>{

	public Course getByName(String name);
	
	@Query(value = "SELECT count(*) FROM user_course where course_id=:course_id", nativeQuery = true)
	public int countStudents(@Param("course_id") int courseId);
	
	/**
	 * If return value equals 0 => no course with this course_id exists
	 * 
	 * @param course_id
	 * @return
	 */
	@Query(value = "SELECT EXISTS(SELECT course_id FROM COURSE c where c.course_id=:course_id)", nativeQuery = true)
	public int doesCourseExist(@Param("course_id") int course_id);
	
	@Query(value = "SELECT c.* FROM course c JOIN user_course uc USING(course_id) WHERE uc.user_id=:student_id", nativeQuery = true)
	public List<Course> getCoursesByStudentId(@Param("student_id") int studentId);
	
	@Query(value = "SELECT c.* FROM course c JOIN exercise e USING(course_id) WHERE e.exercise_id=:exercise_id", nativeQuery = true)
	public Course getByExerciseId(@Param("exercise_id") int exerciseId);
}
