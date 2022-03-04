package io.github.Hattinger04.user.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserForm {
	@NotBlank()
	private String user_id; 
	@NotBlank()
	private String username; 
	@NotBlank()
	private String password; 
	@NotBlank()
	private String email; 
	@NotBlank()
	private String name; 
	@NotBlank()
	private String lastname; 
}
