package com.geoffvargo.securenotes.DTOs;

import com.geoffvargo.securenotes.models.*;

import java.time.*;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private Long userId;
	private String userName;
	private String email;
	private boolean accountNonLocked;
	private boolean accountNonExpired;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private LocalDate credentialsExpiryDate;
	private LocalDate accountExpiryDate;
	private String twoFactorSecret;
	private boolean isTwoFactorEnabled;
	private String signUpMethod;
	private Role role;
	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;
}
