package io.github.Hattinger04;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.Hattinger04.aop.Error;

@RestControllerAdvice
@Component
public class GlobalControlAdvice {

	@Autowired
	private RestServices restServices; 
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception ex, Model model) {
		return restServices.serialize(new Error("Internal server error: GlobalControlAdvice", "Exception occured", HttpStatus.INTERNAL_SERVER_ERROR));  
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public String exceptionDeniedHandler(AccessDeniedException ex, Model model) {
		return restServices.serialize(new Error("Access denied!", "You are not allowed visiting this site!", HttpStatus.FORBIDDEN));
	}
}
