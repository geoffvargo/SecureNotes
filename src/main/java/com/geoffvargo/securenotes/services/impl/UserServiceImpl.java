package com.geoffvargo.securenotes.services.impl;

import com.geoffvargo.securenotes.DTOs.*;
import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.repositories.*;
import com.geoffvargo.securenotes.services.*;
import com.geoffvargo.securenotes.util.*;
import com.warrenstrange.googleauth.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.time.*;
import java.time.temporal.*;
import java.util.*;

@Service
class UserServiceImpl implements UserService {
	@Value("${frontend.url}")
	String frontendUrl;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	TotpService totpService;
	
	@Override
	public void updateUserRole(long userId, String roleName) {
		User user = getUser(userId);
		
		AppRole appRole = AppRole.valueOf(roleName);
		
		Role role = roleRepository.findByRoleName(AppRole.valueOf(roleName)).orElseThrow(
			() -> new RuntimeException("Role not found."));
		
		user.setRole(role);
		
		userRepository.save(user);
	}
	
	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	@Override
	public UserDTO getUserById(Long id) {
		User user = userRepository.findById(id).orElseThrow();
		return convertToDto(user);
	}
	
	private UserDTO convertToDto(User user) {
		return new UserDTO(
			user.getUserId(),
			user.getUserName(),
			user.getEmail(),
			user.isAccountNonLocked(),
			user.isAccountNonExpired(),
			user.isCredentialsNonExpired(),
			user.isEnabled(),
			user.getCredentialsExpiryDate(),
			user.getAccountExpiryDate(),
			user.getTwoFactorSecret(),
			user.isTwoFactorEnabled(),
			user.getSignUpMethod(),
			user.getRole(),
			user.getCreatedDate(),
			user.getUpdatedDate()
		);
	}
	
	@Override
	public User findByUsername(String username) {
		Optional<User> user = userRepository.findByUserName(username);
		return user.orElseThrow(() ->
			                        new RuntimeException("User not found with username: " + username));
	}
	
	@Override
	public void updateLockStatus(Long userId, boolean lock) {
		User user = getUser(userId);
		user.setAccountNonLocked(!lock);
		userRepository.save(user);
	}
	
	@Override
	public List<Role> getAllRoles() {
		return roleRepository.findAll();
	}
	
	@Override
	public void updateAccountExpiryStatus(Long userId, boolean expired) {
		User user = getUser(userId);
		user.setAccountNonExpired(!expired);
		userRepository.save(user);
	}
	
	@Override
	public void updateAccountEnabledStatus(Long userId, Boolean enabled) {
		User user = getUser(userId);
		user.setEnabled(enabled);
		userRepository.save(user);
	}
	
	@Override
	public void updateCredentialsExpiryStatus(Long userId, Boolean expired) {
		User user = getUser(userId);
		user.setCredentialsNonExpired(!expired);
		userRepository.save(user);
	}
	
	@Override
	public void updatePassword(Long userId, String password) {
		User user = getUser(userId);
		
		try {
			user.setPassword(passwordEncoder.encode(password));
			userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException("Failed to update password");
		}
	}
	
	@Override
	public void generatePasswordResetToken(String email) {
		User user = userRepository.findByEmail(email)
			            .orElseThrow(() -> new RuntimeException("User not found"));
		
		String token = UUID.randomUUID().toString();
		Instant expiryDate = Instant.now().plus(24, ChronoUnit.HOURS);
		PasswordResetToken resetToken = new PasswordResetToken(token, expiryDate, user);
		passwordResetTokenRepository.save(resetToken);
		
		String resetUrl = frontendUrl + "/reset-password?token=" + token;
		
		emailService.sendPasswordResetEmail(user.getEmail(), resetUrl);
	}
	
	@Override
	public void resetPassword(String token, String newPassword) {
		PasswordResetToken resetToken =
			passwordResetTokenRepository.findByToken(token)
				.orElseThrow(
					() -> new RuntimeException("Invalid password reset token")
				);
		
		if (resetToken.isUsed()) {
			throw new RuntimeException("Password reset token has already been used");
		}
		
		if (resetToken.getExpiryDate().isBefore(Instant.now())) {
			throw new RuntimeException("Password reset token has expired");
		}
		
		User user = resetToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		
		resetToken.setUsed(true);
		passwordResetTokenRepository.save(resetToken);
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User registerUser(User user) {
		if (user.getPassword() != null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		return userRepository.save(user);
	}
	
	@Override
	public GoogleAuthenticatorKey generate2FAsecret(Long userId) {
		User user = getUser(userId);
		GoogleAuthenticatorKey key = totpService.generateSecret();
		user.setTwoFactorSecret(key.getKey());
		userRepository.save(user);
		return key;
	}
	
	@Override
	public boolean validate2FACode(Long userId, int code) {
		User user = getUser(userId);
		return totpService.verifyCode(user.getTwoFactorSecret(), code);
	}
	
	@Override
	public void enable2FA(Long userId) {
		User user = getUser(userId);
		user.setTwoFactorEnabled(true);
		userRepository.save(user);
	}
	
	@Override
	public void disable2FA(Long userId) {
		User user = getUser(userId);
		user.setTwoFactorEnabled(false);
		userRepository.save(user);
	}
	
	private User getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new RuntimeException("User not found.")
		);
	}
}
