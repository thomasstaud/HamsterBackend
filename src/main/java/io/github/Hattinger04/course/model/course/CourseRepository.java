package io.github.Hattinger04.course.model.course;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.github.Hattinger04.user.model.User;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>{
	Course findById(int id);
	Course findByName(String name);
	// TODO: SQL not tested yet!
	
	@Query(value = "SELECT user_id, username FROM USER u JOIN TEACHER t using(user_id) JOIN course c using(course_id, user_id) where c.course_id=:course_id", nativeQuery = true)
	User getCourseTeacher(int course_id); 
	
	@Query(value = "SELECT user_id, username FROM USER u JOIN STUDENT s using(user_id) JOIN course c using(course_id, user_id) where c.course_id=:course_id", nativeQuery = true)
	List<User> getAllStudents(int course_id); 
	
	@Modifying
	@Query(value = "insert into user_course where user_id = :user_id and course_id=:course_id", nativeQuery = true)
	@Transactional
	public void addUserToCourse(@Param("user_id") int user_id, @Param("course_id") int course_id);
	
	@Modifying
	@Query(value = "delete from user_course where user_id = :user_id and course_id=:course_id", nativeQuery = true)
	@Transactional
	public void removeUserFromCourse(@Param("user_id") int user_id, @Param("course_id") int course_id);
	
	@Query(value = "SELECT count(*) FROM STUDENT s JOIN USER u using(user_id) JOIN course c using(course_id, user_id) where c.course_id=:course_id", nativeQuery = true)
	public int countStudents(@Param("course_id") int course_id); 

	/**
	 * If return value equals 0 => no student with this user_id exists
	 * 
	 * @param user_id
	 * @param course_id
	 * @return
	 */
	@Query(value = "SELECT count(*) FROM STUDENT s JOIN USER u using(user_id) JOIN course c using(course_id, user_id) where c.course_id=:course_id and s.user_id=:user_id", nativeQuery = true)
	public int isUserStudent(@Param("user_id") int user_id, @Param("course_id") int course_id); 
	
	@Query(value = "SELECT count(*) FROM TEACHER t JOIN USER u using(user_id) JOIN course c using(course_id, user_id) where c.course_id=:course_id", nativeQuery = true)
	public int countTeacher(@Param("course_id") int course_id); 
	
	/**
	 * If return value equals 0 => no teacher with this user_id exists
	 * 
	 * @param user_id
	 * @param course_id
	 * @return
	 */
	@Query(value = "SELECT count(*) FROM TEACHER t JOIN USER u using(user_id) JOIN course c using(course_id, user_id) where c.course_id=:course_id and t.user_id=:user_id", nativeQuery = true)
	public int isUserTeacher(@Param("user_id") int user_id, @Param("course_id") int course_id); 
}
