package com.geoffvargo.securenotes.services;

import com.warrenstrange.googleauth.*;

public interface TotpService {
	GoogleAuthenticatorKey generateSecret();
	
	String getQrCodeUrl(GoogleAuthenticatorKey secret, String username);
	
	boolean verifyCode(String secret, int code);
}
