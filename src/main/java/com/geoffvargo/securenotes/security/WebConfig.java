package com.geoffvargo.securenotes.security;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${frontend.url}")
	private String frontendUrl;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// Apply CORS settings only to specific paths
		registry.addMapping("/api/notes/**")
		        .allowedOrigins(frontendUrl)
		        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		        .allowedHeaders("*")
		        .allowCredentials(true)
		        .maxAge(3600);
		
		// Additional paths can be configured similarly
		registry.addMapping("/api/other/**")
		        .allowedOrigins(frontendUrl)
		        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		        .allowedHeaders("*")
		        .allowCredentials(true)
		        .maxAge(3600);
		
		registry.addMapping("/api/csrf-token")
		        .allowedOrigins(frontendUrl)
		        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
		        .allowedHeaders("*")
		        .allowCredentials(true)
		        .maxAge(3600);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/**")
//				        .allowedOrigins(frontendUrl)
//				        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//				        .allowedHeaders("*")
//				        .allowCredentials(true)
//				        .maxAge(3600);
//			}

//		};
//	}
}
