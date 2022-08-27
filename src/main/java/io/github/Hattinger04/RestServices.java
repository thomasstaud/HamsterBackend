package io.github.Hattinger04;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.Hattinger04.user.model.User;

@Service
public class RestServices {
	// TODO: asking how to write better!
	private ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Convert JSON-String to any wanted object
	 * 
	 * @param json
	 * @return
	 */	
	public Object deserialize(Class<?> c, String json) {
		try {
			System.out.println(c == User.class);
			return objectMapper.readValue(json, c);
		} catch (JsonProcessingException e) {
			return null; 
		} 
	}

}
