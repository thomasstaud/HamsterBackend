package io.github.Hattinger04.course.model.course;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long>{
	Course findById(int id);
	Course findByName(String name);
	// TODO: SQL not tested yet!
	
	@Query(value = "SELECT user_id, username FROM USERS u JOIN course c ON u.user_id=c.teacher_id where c.course_id=:course_id", nativeQuery = true)
	List<String[]> getCourseTeacher(int course_id); 
	
	@Query(value = "SELECT user_id, username FROM USER_COURSE uc JOIN USERS u USING(user_id) WHERE uc.course_id=:course_id", nativeQuery = true)
	List<String[]> getAllStudents(int course_id); 
	
	@Query(value = "SELECT EXISTS(SELECT user_id FROM USERS u JOIN STUDENT s using(user_id) JOIN user_course c using(user_id) where user_id=:user_id and c.course_id=:course_id)", nativeQuery = true)
	public int isUserInCourse(@Param("user_id") int user_id, @Param("course_id") int course_id);
	
	@Modifying
	@Query(value = "insert into user_course (user_id, course_id) values (:user_id,:course_id)", nativeQuery = true)
	@Transactional
	public void addUserToCourse(@Param("user_id") int user_id, @Param("course_id") int course_id);
	
	@Modifying
	@Query(value = "delete from user_course where user_id = :user_id and course_id=:course_id", nativeQuery = true)
	@Transactional
	public void removeUserFromCourse(@Param("user_id") int user_id, @Param("course_id") int course_id);
	
	@Query(value = "SELECT count(*) FROM user_course where course_id=:course_id", nativeQuery = true)
	public int countStudents(@Param("course_id") int course_id); 

	/**
	 * If return value equals 0 => no student with this user_id exists
	 * 
	 * @param user_id
	 * @param course_id
	 * @return
	 */
	@Query(value = "SELECT EXISTS(SELECT * FROM user_course where course_id=:course_id and user_id=:user_id)", nativeQuery = true)
	public int isUserStudent(@Param("user_id") int user_id, @Param("course_id") int course_id);
	
	/**
	 * If return value equals 0 => no teacher with this user_id exists
	 * 
	 * @param user_id
	 * @param course_id
	 * @return
	 */
	@Query(value = "SELECT EXISTS(SELECT teacher_id FROM course where course_id=:course_id and user_id=:user_id)", nativeQuery = true)
	public int isUserTeacher(@Param("user_id") int user_id, @Param("course_id") int course_id); 
	
	/**
	 * If return value equals 0 => no course with this course_id exists
	 * 
	 * @param course_id
	 * @return
	 */
	@Query(value = "SELECT EXISTS(SELECT course_id FROM COURSE c where c.course_id=:course_id)", nativeQuery = true)
	public int doesCourseExist(@Param("course_id") int course_id);
	
	@Query(value = "SELECT c.* FROM course c JOIN user_course uc USING(course_id) WHERE uc.user_id=:student_id", nativeQuery = true)
	public List<Course> findCourseByStudentId(@Param("student_id") int student_id);
}
