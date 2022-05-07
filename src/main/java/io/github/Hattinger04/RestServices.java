package io.github.Hattinger04;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.Hattinger04.hamster.model.Hamster;
import io.github.Hattinger04.user.model.User;

@Service
public class RestServices {

	private ObjectMapper objectMapper = new ObjectMapper();
	public final String jsonErrorMsg = "[{\"error\", \"json\"}]"; 
	
	public String serialize(Object msg) {
		try {
			return objectMapper.writeValueAsString(msg);
		} catch (JsonProcessingException e) {
			return this.jsonErrorMsg; 
		}
	}
	
	public User deserializeUser(String json) {
		try {
			return objectMapper.readValue(json, User.class);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
	public Hamster deserializeHamster(String json) {
		try {
			return objectMapper.readValue(json, Hamster.class);
		} catch (JsonProcessingException e) {
			return null;
		}
	}
	
	public String errorMessage(String msg) {
		try {
			return objectMapper.writeValueAsString(new String[]{"error", msg}); 
		} catch (JsonProcessingException e) {
			return this.jsonErrorMsg; 
		}
	}
}
