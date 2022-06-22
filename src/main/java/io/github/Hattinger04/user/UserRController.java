package io.github.Hattinger04.user;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> getAllUsers() {
		return new ResponseEntity<>(userService.selectMany(), HttpStatus.OK); 
	}
	
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/createUser")
	@ResponseBody
	public ResponseEntity<?> createUser(@RequestBody String json) {
		User user = restServices.deserializeUser(json);
		if(user == null) {
			return new ResponseEntity<>("wrong data", HttpStatus.BAD_REQUEST);
		}
		if(!userService.saveUser(user)) {
			return new ResponseEntity<>("error in db", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
		
	@GetMapping("/home")
	public ResponseEntity<?>  successHome() {
		return new ResponseEntity<>((SecurityContextHolder.getContext().getAuthentication().getName()), HttpStatus.OK);
	}
	
	@GetMapping("/logout")
	@PreAuthorize("isAuthenticated()") 
	public ResponseEntity<?> logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/updateUser")
	@PreAuthorize("hasAuthority('Admin')")
	public ResponseEntity<?> updateUser(@RequestBody String json) {
		User user = restServices.deserializeUser(json); 
		if(userService.updateUser(user)) {
			return new ResponseEntity<>("Could not update user!", HttpStatus.NOT_FOUND); 
		}
		return new ResponseEntity<>(HttpStatus.OK); 
	}
}
