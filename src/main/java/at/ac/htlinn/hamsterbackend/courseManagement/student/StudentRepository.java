package at.ac.htlinn.hamsterbackend.courseManagement.student;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.htlinn.hamsterbackend.user.model.User;

public interface StudentRepository extends JpaRepository<User, Integer>{

	@Query(value = "SELECT u.* FROM USER_ROLE ur JOIN USERS u USING(user_id) WHERE ur.role_id=4", nativeQuery = true)
	public List<User> getAllStudents();
	
	@Query(value = "SELECT u.* FROM USER_COURSE uc JOIN USERS u USING(user_id) WHERE uc.course_id=:course_id", nativeQuery = true)
	public List<User> getAllStudentsInCourse(@Param("course_id") int courseId);
	
	@Query(value = "SELECT EXISTS(SELECT user_id FROM USERS u JOIN STUDENT s using(user_id) JOIN user_course c using(user_id) "
			+ "WHERE user_id=:user_id AND c.course_id=:course_id)", nativeQuery = true)
	public int isUserInCourse(@Param("user_id") int userId, @Param("course_id") int courseId);
	
	@Modifying
	@Query(value = "INSERT INTO user_course (user_id, course_id) VALUES (:user_id,:course_id)", nativeQuery = true)
	@Transactional
	public void addUserToCourse(@Param("user_id") int userId, @Param("course_id") int courseId);
	
	@Modifying
	@Query(value = "DELETE FROM user_course WHERE user_id = :user_id AND course_id=:course_id", nativeQuery = true)
	@Transactional
	public void removeUserFromCourse(@Param("user_id") int userId, @Param("course_id") int courseId);
	
	@Modifying
	@Query(value = "DELETE FROM user_course WHERE course_id=:course_id", nativeQuery = true)
	@Transactional
	public void removeAllUsersFromCourse(@Param("course_id") int courseId);

	/**
	 * If return value equals 0 => no student with this user_id exists
	 * 
	 * @param user_id
	 * @param course_id
	 * @return
	 */
	@Query(value = "SELECT EXISTS(SELECT * FROM user_course WHERE course_id=:course_id AND user_id=:user_id)", nativeQuery = true)
	public int isUserStudent(@Param("user_id") int userId, @Param("course_id") int courseId);
}
