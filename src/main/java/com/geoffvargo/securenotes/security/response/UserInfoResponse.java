package com.geoffvargo.securenotes.security.response;

import java.time.*;
import java.util.*;

import lombok.*;

@Data
public class UserInfoResponse {
	private Long id;
	private String username;
	private String email;
	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private LocalDate credentialsExpiryDate;
	private LocalDate accountExpiryDate;
	private boolean isTwoFactorEnabled;
	private List<String> roles;
	
	public UserInfoResponse(Long id,
	                        String username,
	                        String email,
	                        boolean accountNonLocked,
	                        boolean accountNonExpired,
	                        boolean credentialsNonExpired,
	                        boolean enabled,
	                        LocalDate credentialsExpiryDate,
	                        LocalDate accountExpiryDate,
	                        boolean isTwoFactorEnabled,
	                        List<String> roles) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.accountNonLocked = accountNonLocked;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.enabled = enabled;
		this.credentialsExpiryDate = credentialsExpiryDate;
		this.accountExpiryDate = accountExpiryDate;
		this.isTwoFactorEnabled = isTwoFactorEnabled;
		this.roles = roles;
	}
}
