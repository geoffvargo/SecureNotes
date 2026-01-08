package com.geoffvargo.securenotes.security.request;

import lombok.*;

@Getter
@Setter
public class LoginRequest {
	private String username;
	private String password;
}
