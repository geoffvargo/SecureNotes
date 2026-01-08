package com.geoffvargo.securenotes.security.response;

import java.util.*;

import lombok.*;

@Getter
@Setter
public class LoginResponse {
	private String jwtToken;
	private String username;
	private List<String> roles;
	
	public LoginResponse(String username, List<String> roles, String jwtToken) {
		this.jwtToken = jwtToken;
		this.username = username;
		this.roles = roles;
	}
}
