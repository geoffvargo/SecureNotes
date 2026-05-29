package com.geoffvargo.securenotes.security;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

import lombok.extern.slf4j.*;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {
	//	@Value("${frontend.url}")
	@Value("http://localhost:4200")
	private String frontendUrl;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		log.info("frontendUrl = {}", frontendUrl);
		registry.addMapping("/**")
			.allowedOrigins(frontendUrl, "http://localhost:3000")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowedHeaders("*")
			.allowCredentials(true)
			.maxAge(3600);
	}
}
