package io.github.Hattinger04.user.model;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository rep;
	
	public User findOne(int id) {
		Map<String, Object> map = rep.findOne(id); 
		if(map == null) {
			return null; 
		}
		String name = (String) map.get("username"); 
		String password = (String) map.get("password"); 
		
		User user = new User(); 
		user.setUserId(id);
		user.setUsername(name);
		user.setPassword(password);
		return user; 
	}
}
