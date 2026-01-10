package com.geoffvargo.securenotes.security.response;

import lombok.*;

@Data
public class MessageResponse {
	private String message;
	
	public MessageResponse(String message) {
		this.message = message;
	}
}
