package com.geoffvargo.securenotes.services.impl;

import com.geoffvargo.securenotes.DTOs.*;
import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.repositories.*;
import com.geoffvargo.securenotes.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Override
	public void updateUserRole(long userId, String roleName) {
		User user = userRepository.findById(userId).orElseThrow(
			() -> new RuntimeException("User not found."));
		
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
}
