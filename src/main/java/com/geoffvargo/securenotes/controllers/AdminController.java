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
	
	@GetMapping("/user/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
		return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
	}
}
