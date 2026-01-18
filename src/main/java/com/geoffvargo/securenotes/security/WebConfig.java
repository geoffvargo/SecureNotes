package com.geoffvargo.securenotes.security;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${frontend.url}")
	private String frontendUrl;
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				        .allowedOrigins(frontendUrl)
				        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				        .allowedHeaders("*")
				        .allowCredentials(true)
				        .maxAge(3600);
			}
		};
	}
}
