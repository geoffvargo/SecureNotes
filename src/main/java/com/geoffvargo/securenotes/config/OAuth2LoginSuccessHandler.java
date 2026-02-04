package com.geoffvargo.securenotes.config;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.repositories.*;
import com.geoffvargo.securenotes.security.jwt.*;
import com.geoffvargo.securenotes.security.services.*;
import com.geoffvargo.securenotes.services.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.context.*;
import org.springframework.security.oauth2.client.authentication.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.security.web.authentication.*;
import org.springframework.stereotype.*;
import org.springframework.web.util.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.*;
import lombok.extern.slf4j.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired
	private final UserService userService;
	
	@Autowired
	private final JwtUtils jwtUtils;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Value("${frontend.url}")
	private String frontendUrl;
	
	String username;
	String idAttributeKey;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
	throws ServletException, IOException {
		OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
		
		if ("github".equals(token.getAuthorizedClientRegistrationId()) ||
			 "google".equals(token.getAuthorizedClientRegistrationId())) {
			DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
			Map<String, Object> attributes = principal.getAttributes();
			
			String email = attributes.getOrDefault("email", "").toString();
			String name = attributes.getOrDefault("name", "").toString();
			
			if ("github".equals(token.getAuthorizedClientRegistrationId())) {
				username = attributes.getOrDefault("login","").toString();
				idAttributeKey = "sub";
			} else if ("ggogle".equals(token.getAuthorizedClientRegistrationId())) {
				username = email.split("@")[0];
				idAttributeKey = "id";
			} else {
				username = "";
				idAttributeKey = "id";
			}
			log.info("HELLO OAUTH: {} : {} : {}", email, name, username);
			
			userService.findByEmail(email)
						  .ifPresentOrElse(user -> {
								  DefaultOAuth2User oauthUser = new DefaultOAuth2User(
									  List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
									  attributes,
									  idAttributeKey
								  );
								  Authentication securityAuth = new OAuth2AuthenticationToken(
									  oauthUser,
									  List.of(new SimpleGrantedAuthority(user.getRole().getRoleName().name())),
									  token.getAuthorizedClientRegistrationId()
								  );
								  SecurityContextHolder.getContext().setAuthentication(securityAuth);
							  }, () -> {
								  User newUser = new User();
								  Optional<Role> userRole = roleRepository.findByRoleName(AppRole.ROLE_USER);
								  
								  if (userRole.isPresent()) {
									  newUser.setRole(userRole.get());
								  } else {
									  throw new RuntimeException("Default role not found");
								  }
								  newUser.setEmail(email);
								  newUser.setUserName(username);
								  newUser.setSignUpMethod(token.getAuthorizedClientRegistrationId());
								  userService.registerUser(newUser);
								  DefaultOAuth2User oauthUser = new DefaultOAuth2User(
									  List.of(new SimpleGrantedAuthority((newUser.getRole().getRoleName().name()))),
									  attributes,
									  idAttributeKey
								  );
								  Authentication securityAuth = new OAuth2AuthenticationToken(
									  oauthUser,
									  List.of(new SimpleGrantedAuthority(newUser.getRole().getRoleName().name())),
									  token.getAuthorizedClientRegistrationId()
								  );
								  SecurityContextHolder.getContext().setAuthentication(securityAuth);
							  }
						  );
		}
		this.setAlwaysUseDefaultTargetUrl(true);
		
		// JWT token logic
		DefaultOAuth2User oauth2User = (DefaultOAuth2User) authentication.getPrincipal();
		Map<String, Object> attributes = oauth2User.getAttributes();
		
		// Extract necessary attributes
		String email = (String) attributes.get("email");
		log.info("OAuth2LoginSuccessHandler: {} : {}", username, email);
		
		// Create UserDetailsImpl instance
		UserDetailsImpl userDetails = new UserDetailsImpl(
			null,
			username,
			email,
			null,
			false,
			oauth2User.getAuthorities().stream()
						 .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
						 .collect(Collectors.toList())
		);
		
		// Generate JWT token
		String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
		
		// Redirect to the frontend with the JWT token
		String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
															.queryParam("token", jwtToken)
									 .build().toUriString();
		this.setDefaultTargetUrl(targetUrl);
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
