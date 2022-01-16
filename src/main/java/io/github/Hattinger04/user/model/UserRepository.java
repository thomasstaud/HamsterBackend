package io.github.Hattinger04.user.model;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	
	@Autowired
	private JdbcTemplate db; 
	
	public Map<String, Object> findOne(int id) {
		String sql = "SELECT user_id, username, password FROM user WHERE user_id = ?"; 
		try {
			Map<String, Object> result = db.queryForMap(sql, id); 
			return result; 
		} catch (EmptyResultDataAccessException ex) {
			return null; 
		}
		
	}
}
