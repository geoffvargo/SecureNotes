package com.geoffvargo.securenotes.util;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.repositories.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;

@Component
public class AuthUtil {
	@Autowired
	private UserRepository userRepository;
	
	public Long LoggedInUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByUserName(auth.getName()).orElseThrow(
			() -> new RuntimeException("Username not found"));
		return user.getUserId();
	}
	
	public User LoggedInUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return  userRepository.findByUserName(auth.getName()).orElseThrow(
			() -> new RuntimeException("Username not found"));
	}
}
