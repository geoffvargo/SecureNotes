package com.geoffvargo.securenotes.util;

import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendPasswordResetEmail(String to, String resetUrl) {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setTo(to);
		message.setSubject("Password Reset Request");
		message.setText("Click the link to reset your password: " + resetUrl);
		
		mailSender.send(message);
	}
}
