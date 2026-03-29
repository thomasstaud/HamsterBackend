package at.ac.htlinn.hamsterbackend.configuration;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
public class Error {

	private String error; 
	private String message; 
	private HttpStatus status; 
	
}
