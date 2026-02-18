package com.geoffvargo.securenotes.services.impl;

import com.geoffvargo.securenotes.services.*;
import com.warrenstrange.googleauth.*;

import org.springframework.stereotype.*;

@Service
class TotpServiceImpl implements TotpService {
	private final GoogleAuthenticator gAuth;
	
	public TotpServiceImpl(GoogleAuthenticator gAuth) {
		this.gAuth = gAuth;
	}
	
	TotpServiceImpl() {
		gAuth = new GoogleAuthenticator();
	}
	
	@Override
	public GoogleAuthenticatorKey generateSecret() {
		return gAuth.createCredentials();
	}
	
	@Override
	public String getQrCodeUrl(GoogleAuthenticatorKey secret, String username) {
		return GoogleAuthenticatorQRGenerator.getOtpAuthTotpURL("Secure Notes Application", username, secret);
	}
	
	@Override
	public boolean verifyCode(String secret, int code) {
		return gAuth.authorize(secret, code);
	}
}
