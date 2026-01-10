package com.geoffvargo.securenotes.security.request;

import java.util.*;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class SignupRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;
	
	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	@Getter
	@Setter
	private Set<String> role;
	
	@NotBlank
	@Size(min = 6, max = 40)
	private String password;
}
