package io.github.Hattinger04.aop;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class GlobalControlAdvice {

	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception ex, Model model) {
		model.addAttribute("error", "Internal server error: GlobalControlAdvice"); 
		model.addAttribute("message", "Exception occured"); 
		model.addAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR); 
		return "error"; 
	}
}
