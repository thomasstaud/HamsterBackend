package io.github.Hattinger04.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@RestController
@RequestMapping("/user")
public class UserRController {

	@Autowired
	private UserService userService;
	ObjectMapper objectMapper = new ObjectMapper();


//	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/getAllUsers")
	public String getAllUsers() {
		try {
			return objectMapper.writeValueAsString(userService.selectMany());
		} catch (JsonProcessingException e) {
			return "error - json"; 
		}
	}
	
//	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/createUser")
	public String createUser(@RequestBody String json) {
		User user = userService.deserializeUser(json);
		if(user != null && userService.saveUser(user)) {
			return "true";
		}
		
		return "dbinsert - error"; 
	}
}
