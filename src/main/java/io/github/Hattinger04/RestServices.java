package io.github.Hattinger04;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RestServices {
	private ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Convert JSON-String to any wanted object
	 * 
	 * @param json
	 * @return
	 */	
	public Object deserialize(Class<?> c, String json) {
		try {
			return objectMapper.readValue(json, c);
		} catch (JsonProcessingException e) {
			return null; 
		} 
	}
	
	/**
	 * Convert JSON-String with multiple different classes to multiple objects
	 * Convention: list the classes in an alphabetic sequence 
	 * 
	 * @param <T>
	 * @param c
	 * @param json
	 * @return
	 */
	public Object[] deserializeMany(Class<?> c[], String json) {
		try {
			JsonNode rootNode = objectMapper.readTree(json);
			Object[] objects = new Object[c.length]; 
			int arr = 0; 
			Iterator<String> iterate = rootNode.fieldNames(); 
			for(JsonNode node : rootNode) {
				objects[arr] = objectMapper.readValue(String.format("{\"%s\":%s}" , iterate.next().toString(), node.toString()) , c[arr]);
				arr++; 
			}
			return objects; 
		} catch(ConcurrentModificationException e) {
			System.out.println(e.getLocalizedMessage());
			return null; 
		} catch (JsonProcessingException e) {
			System.out.println("error: " + e.getLocalizedMessage());
			return null;
		} 
	}
}
