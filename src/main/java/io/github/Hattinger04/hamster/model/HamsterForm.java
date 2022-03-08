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
	private String leange, breite;
	@NotBlank
	private String corn; 
	@NotBlank
	private String cornAnzahl; 
	@NotBlank
	private String wall; 
	@NotBlank
	private String x, y;
}
