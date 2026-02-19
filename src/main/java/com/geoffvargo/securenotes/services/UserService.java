package com.geoffvargo.securenotes.services;

import com.geoffvargo.securenotes.DTOs.*;
import com.geoffvargo.securenotes.models.*;
import com.warrenstrange.googleauth.*;

import java.util.*;

public interface UserService {
	void updateUserRole(long userId, String roleName);
	
	List<User> getAllUsers();
	
	UserDTO getUserById(Long id);
	
	User findByUsername(String usernString);
	
	void updateLockStatus(Long userId, boolean lock);
	
	List<Role> getAllRoles();
	
	void updateAccountExpiryStatus(Long userId, boolean expired);
	
	void updateAccountEnabledStatus(Long userId, Boolean enabled);
	
	void updateCredentialsExpiryStatus(Long userId, Boolean expired);
	
	void updatePassword(Long userId, String password);
	
	void generatePasswordResetToken(String email);
	
	void resetPassword(String token, String newPassword);
	
	Optional<User> findByEmail(String email);
	
	User registerUser(User user);
	
	GoogleAuthenticatorKey generate2FAsecret(Long userId);
	
	boolean validate2FACode(Long userId, int code);
	
	void enable2FA(Long userId);
	
	void disable2FA(Long userId);
}
