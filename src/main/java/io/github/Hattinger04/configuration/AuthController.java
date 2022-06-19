package io.github.Hattinger04.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.github.Hattinger04.user.model.User;

@CrossOrigin(origins = "https://localhost",allowCredentials = "true")
@RestController
public class AuthController {
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/login")
	public ResponseEntity<HttpStatus> login(@RequestBody User user) throws Exception {	
		try {
			SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())));
		} catch (BadCredentialsException e) {
			throw new Exception("Invalid credentials");
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
}
