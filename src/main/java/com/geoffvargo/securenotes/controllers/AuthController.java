package com.geoffvargo.securenotes.controllers;

import com.geoffvargo.securenotes.security.jwt.*;
import com.geoffvargo.securenotes.security.request.*;
import com.geoffvargo.securenotes.security.response.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
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
}
