package at.ac.htlinn.hamsterbackend.security;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import at.ac.htlinn.hamsterbackend.role.model.Role;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

import java.security.Principal;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) throws Exception {
		try {
			SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())));
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid credentials");
		}
		user = userService.findUserByUsername(user.getUsername());
		return ResponseEntity.ok(user.getRoles());
	}
	
	// TODO: is checking data with constraints working?
	@PostMapping("/registration")
	public ResponseEntity<?> register(@RequestBody User user) throws Exception {
		User userExists = userService.findUserByUsername(user.getUsername());
		if (userExists != null) {
			return new ResponseEntity<>("Es gibt bereits einen User mit diesem Namen", HttpStatus.BAD_REQUEST); 
		}
		return userService.saveUser(user) != null ?
				ResponseEntity.ok(null) : new ResponseEntity<>("Invalid Credentials", HttpStatus.BAD_REQUEST); 
	}
	
	
	@GetMapping("login")
	public ResponseEntity<?> isLoggedIn() {
		if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); 
		}
		return new ResponseEntity<>(HttpStatus.OK); 
	}
	
	@GetMapping("/logout")
	@PreAuthorize("isAuthenticated()") // not really needed
	public ResponseEntity<?> logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
