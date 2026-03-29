package at.ac.htlinn.hamsterbackend.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import at.ac.htlinn.hamsterbackend.user.model.User;
import lombok.Data;

@Data
public class UserDto {
	public UserDto(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
	}

	@JsonProperty("id")
	private int id;
	@JsonProperty("username")
	private String username;
}
