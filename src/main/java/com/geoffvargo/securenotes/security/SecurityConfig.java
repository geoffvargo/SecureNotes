package com.geoffvargo.securenotes.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.*;
import org.springframework.security.web.*;

import javax.sql.*;

import static org.springframework.security.config.Customizer.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> requests
			                                       .anyRequest().authenticated());
		http.csrf(AbstractHttpConfigurer::disable);
		// http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService(DataSource datasource) {
		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(datasource);
		
		if (!manager.userExists("user1")) {
			manager.createUser(
				User.withUsername("user1")
				    .password("{noop}password1")
				    .roles("USER")
					.build()
			);
		}
		
		if (!manager.userExists("admin")) {
			manager.createUser(
				User.withUsername("admin")
				    .password("{noop}adminPass")
				    .roles("ADMIN")
					.build()
			);
		}
		
		return manager;
	}
}
