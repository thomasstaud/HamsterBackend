package at.ac.htlinn.hamsterbackend.user;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.htlinn.hamsterbackend.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findById(int id);
	User findByUsername(String username);
	
	@Query(value = "SELECT auto_increment FROM information_schema.tables WHERE table_name='users'", nativeQuery = true)
	List<Long> getNextSeriesId();
	
	@Modifying
	@Query(value = "Insert into user_role(user_id, role_id) VALUES (:user_id, :role_id)", nativeQuery = true)
	@Transactional
	public void insertUserRole(@Param("user_id") long user_id, @Param("role_id") long role_id); 
	
	@Modifying
	@Query(value = "delete from user_role where user_id = :user_id and role_id=:role_id", nativeQuery = true)
	@Transactional
	public void removeUserRole(@Param("user_id") long user_id, @Param("role_id") long role_id); 
	
}