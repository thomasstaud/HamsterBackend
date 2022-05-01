package io.github.Hattinger04.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.Hattinger04.user.model.UserService;

@RestController
public class UserRController {

	@Autowired
	private UserService userService;

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/user/getAllUsers")
	public String getAllUsers() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(userService.selectMany());
		} catch (JsonProcessingException e) {
			return "error - json"; 
		} 
	}
}
