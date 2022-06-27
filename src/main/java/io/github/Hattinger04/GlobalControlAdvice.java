package io.github.Hattinger04;

import org.hibernate.exception.DataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.Hattinger04.aop.Error;

@RestControllerAdvice
@Component
public class GlobalControlAdvice {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> exceptionHandler(Exception ex, Model model) {
		return new ResponseEntity<>(new Error("Internal server error: GlobalControlAdvice", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);  
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> exceptionDeniedHandler(AccessDeniedException ex, Model model) {
		return new ResponseEntity<>(new Error("Access denied!", "You are not allowed visiting this site!", HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN); 
	}
	
	@ExceptionHandler(DataException.class)
	public ResponseEntity<?> exceptionDeniedHandler(DataException ex, Model model) {
		return new ResponseEntity<>(new Error("Wrong sql!", "You have made some mistake in your sql statement!", HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR); 
	}
}
