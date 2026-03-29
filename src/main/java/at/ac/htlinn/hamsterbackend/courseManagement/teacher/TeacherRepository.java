package at.ac.htlinn.hamsterbackend.courseManagement.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.htlinn.hamsterbackend.user.model.User;

public interface TeacherRepository extends JpaRepository<User, Integer>{
	
	@Query(value = "SELECT u.* FROM USERS u JOIN course c ON u.user_id=c.teacher_id where c.course_id=:course_id", nativeQuery = true)
	public User getCourseTeacher(@Param("course_id") int courseId);
	
	/**
	 * If return value equals 0 => no teacher with this user_id exists
	 * 
	 * @param user_id
	 * @param course_id
	 * @return
	 */
	@Query(value = "SELECT EXISTS(SELECT teacher_id FROM course "
			+ "WHERE course_id=:course_id AND teacher_id=:user_id)",nativeQuery = true)
	public int isUserTeacher(@Param("user_id") int user_id, @Param("course_id") int courseId);
}
