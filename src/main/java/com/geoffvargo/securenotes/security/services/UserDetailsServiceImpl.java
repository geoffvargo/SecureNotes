package com.geoffvargo.securenotes.security.services;

import com.geoffvargo.securenotes.repositories.*;

import com.geoffvargo.securenotes.models.User;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import jakarta.transaction.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;
	
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(username).orElseThrow(
			() -> new UsernameNotFoundException("User Not Found with username: " + username)
		);
		
		return UserDetailsImpl.build(user);
	}
}
