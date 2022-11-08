package io.github.Hattinger04.hamster.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

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
@ToString@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME, property="type")
@JsonTypeName("hamster") 
public class Hamster {
	
	public Hamster(String program, String programName) {
		this.program = program; 
		this.programName = programName; 
	}
	
	@Id
	private Integer hamster_id;
	private String programName; 
	private String terrainName;
	private String program; 
	private int laenge, breite, x, y, blickrichtung;
	private int[] cornAnzahl; 
	private int[][] corn, wall; 
}
