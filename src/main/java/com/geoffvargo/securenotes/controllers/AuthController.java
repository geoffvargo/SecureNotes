package com.geoffvargo.securenotes.controllers;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.models.User;
import com.geoffvargo.securenotes.repositories.*;
import com.geoffvargo.securenotes.security.jwt.*;
import com.geoffvargo.securenotes.security.request.*;
import com.geoffvargo.securenotes.security.response.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

import jakarta.validation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
	@PostMapping("/public/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
		Authentication authentication;
		
		try {
			authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (AuthenticationException e) {
			Map<String, Object> map = new HashMap<>();
			
			map.put("message", "Bad credentials");
			map.put("status", false);
			
			return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
		}
		
		// set the authentication
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// specific to our implementation
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
		
		// Collect roles from the UserDetails
		List<String> roles = userDetails.getAuthorities().stream()
		                                .map(GrantedAuthority::getAuthority)
		                                .toList();
		
		// Prepare the response body, now including the JWT token directly in the body
		LoginResponse response = new LoginResponse(userDetails.getUsername(), roles, jwtToken);
		
		// Return the response entity with the JWT token included in the response body
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/public/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest request) {
		if (userRepository.existsByUserName(request.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}
		
		if (userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		
		// Create new user's account
		User user = new User(request.getUsername(),
		                     request.getEmail(),
		                     encoder.encode(request.getPassword()));
		
		Set<String> strRoles = request.getRole();
		Role role;
		
		if (strRoles == null || strRoles.isEmpty()) {
			role = roleRepository.findByRoleName(AppRole.ROLE_USER)
			                     .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		} else {
			String roleStr = strRoles.iterator().next();
			
			if (roleStr.equals("admin")) {
				role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
				                     .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			} else {
				role = roleRepository.findByRoleName(AppRole.ROLE_USER)
				                     .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			}
			
			user.setAccountNonLocked(true);
			user.setAccountNonExpired(true);
			user.setCredentialsNonExpired(true);
			user.setEnabled(true);
			user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
			user.setAccountExpiryDate(LocalDate.now().plusYears(1));
			user.setTwoFactorEnabled(false);
			user.setSignUpMethod("email");
		}
		
		user.setRole(role);
		userRepository.save(user);
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
