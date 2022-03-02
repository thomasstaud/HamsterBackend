package io.github.Hattinger04.user.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findById(int id);
	User findByEmail(String email);
	User findByUsername(String username);
	@Query(value = "SELECT auto_increment FROM information_schema.tables WHERE table_name='users'", nativeQuery = true)
	List<Long> getNextSeriesId();
}