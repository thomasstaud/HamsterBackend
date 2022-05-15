package io.github.Hattinger04.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.Hattinger04.RestServices;
import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;

@RestController
@RequestMapping("/user")
public class UserRController {

	@Autowired
	private UserService userService;
	@Autowired
	private RestServices restServices; 
	

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/getAllUsers")
	public String getAllUsers() {
		return restServices.serialize(userService.selectMany()); 
	}
	
//	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/createUser")
	@ResponseBody
	public String createUser(@RequestBody String json) {
		User user = restServices.deserializeUser(json);
		if(user == null) {
			return restServices.errorMessage("wrong data");
		}
		if(!userService.saveUser(user)) {
			return restServices.errorMessage("error in db");
		}
		return restServices.serialize(user); 
	}
		
	@GetMapping("/home")
	public String successHome() {
		return restServices.serialize(SecurityContextHolder.getContext().getAuthentication().getName());
	}
}
