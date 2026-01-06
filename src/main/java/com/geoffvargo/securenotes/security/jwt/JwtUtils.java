package com.geoffvargo.securenotes.security.jwt;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

import java.security.*;
import java.util.*;

import javax.crypto.*;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.*;
import io.jsonwebtoken.security.*;
import jakarta.servlet.http.*;

@Component
public class JwtUtils {
	public static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value("${spring.app.jwtSecret}")
	private String jwtSecret;
	
	@Value("${spring.app.jwtExpirationMs}")
	private int jwtExpirationMs;
	
	public String getJwtFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		LOGGER.debug("Authorization Header: {}", bearerToken);
		
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		
		return null;
	}
	
	public String generateTokenFromUsername(UserDetails userDetails) {
		String username = userDetails.getUsername();
		
		return Jwts.builder()
			       .subject(username)
			       .issuedAt(new Date((new Date()).getTime() + jwtExpirationMs))
			       .signWith(key())
			       .compact();
	}
	
	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser()
		           .verifyWith((SecretKey) key())
			       .build()
			       .parseSignedClaims(token)
			       .getPayload()
			       .getSubject();
	}
	
	public boolean validateJwtToken(String authToken) {
		try {
			System.out.println("Validate");
			
			Jwts.parser()
			    .verifyWith((SecretKey) key())
				.build()
				.parseSignedClaims(authToken);
			
			return true;
		} catch (MalformedJwtException e) {
			LOGGER.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			LOGGER.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			LOGGER.error("JWT toke is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.error("JWT claims string is ermpty: {}", e.getMessage());
		}
		
		return false;
	}
}
