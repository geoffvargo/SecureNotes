package com.geoffvargo.securenotes.controllers;

import com.geoffvargo.securenotes.DTOs.*;
import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
	@Autowired
	UserService userService;
	
	@GetMapping("/getusers")
	public ResponseEntity<List<User>> getAllUsers() {
		return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
	}
	
	@PutMapping("/update-role")
	public ResponseEntity<String> updateUserRole(@RequestParam Long userId,
	                                             @RequestParam String roleName) {
		userService.updateUserRole(userId, roleName);
		return ResponseEntity.ok("User role updated.");
	}
	
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
	}
	
	@PutMapping("/update-lock-status")
	public ResponseEntity<String> updateAccountLockStatus(@RequestParam Long userId,
	                                                      @RequestParam boolean lock) {
		userService.updateLockStatus(userId, lock);
		return ResponseEntity.ok("Account lock status updated.");
	}
	
	@GetMapping("/roles")
	public List<Role> getAllRoles() {
		return userService.getAllRoles();
	}
	
	@PutMapping("/update-expiry-status")
	public ResponseEntity<String> updateAccountExpiryStatus(@RequestParam Long userId,
	                                                        @RequestParam boolean expired) {
		userService.updateAccountExpiryStatus(userId, expired);
		return ResponseEntity.ok("Account expiry status updated");
	}
	
	@PutMapping("/update-enabled-status")
	public ResponseEntity<String> updateAccountEnabledStatus(@RequestParam Long userId,
	                                                         @RequestParam boolean enabled) {
		userService.updateAccountEnabledStatus(userId, enabled);
		return ResponseEntity.ok("Account enabled status updated");
	}
	
	@PutMapping("/update-credentials-expiry-status")
	public ResponseEntity<?> updateCredentialsExpiryStatus(@RequestParam Long userId,
	                                                       @RequestParam boolean expired) {
		userService.updateCredentialsExpiryStatus(userId, expired);
		return ResponseEntity.ok("Credentials expiry status updated");
	}
	
	@PutMapping("")
	public ResponseEntity<?> updatePassword(@RequestParam Long userId,
	                                              @RequestParam String password) {
		try {
			userService.updatePassword(userId, password);
			return ResponseEntity.ok("Password updated");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
