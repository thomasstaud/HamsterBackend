package io.github.Hattinger04.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserDTO {
	public UserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
	}

	@JsonProperty("id")
	private int id;
	@JsonProperty("username")
	private String username;
}
