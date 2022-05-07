package io.github.Hattinger04.hamster.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class Hamster {
	@Id
	private Integer hamster_id;
	private String programName; 
	private String program; 
	private int leange, breite, x, y;
	private int[] cornAnzahl; 
	private int[][] corn, wall;  
}
