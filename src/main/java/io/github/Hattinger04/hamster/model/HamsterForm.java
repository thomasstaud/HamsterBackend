package io.github.Hattinger04.hamster.model;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class HamsterForm {
	@NotBlank()
	private String hamster_id; 
	@NotBlank()
	private String program; 
	@NotBlank
	private String terrain; 
}
