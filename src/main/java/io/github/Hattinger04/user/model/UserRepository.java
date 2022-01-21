package io.github.Hattinger04.user.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findById(int id); 
    User findByEmail(String email);
    User findByUsername(String username);
   
}