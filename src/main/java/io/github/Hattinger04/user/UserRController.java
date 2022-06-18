package io.github.Hattinger04.user;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.Hattinger04.RestServices;
import io.github.Hattinger04.user.model.User;
import io.github.Hattinger04.user.model.UserService;

@CrossOrigin(origins = "https://localhost:8081",allowCredentials = "true")
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
	
	@PreAuthorize("hasAuthority('ADMIN')")
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
	
	@GetMapping("/logout")
	@PreAuthorize("isAuthenticated()") 
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return restServices.serialize("logged out");
	}
}
