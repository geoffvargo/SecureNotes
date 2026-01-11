package com.geoffvargo.securenotes.services;

import com.geoffvargo.securenotes.DTOs.*;
import com.geoffvargo.securenotes.models.*;

import java.util.*;

public interface UserService {
	void updateUserRole(long userId, String roleName);
	
	List<User> getAllUsers();
	
	UserDTO getUserById(Long id);
	
	User findByUsername(String usernString);
}
