package com.geoffvargo.securenotes.security;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.models.Role;
import com.geoffvargo.securenotes.repositories.*;

import org.springframework.boot.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;

import java.time.*;

import static org.springframework.security.config.Customizer.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
                      securedEnabled = true,
                      jsr250Enabled = true)
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((requests)
			                           -> requests
				                              .requestMatchers("/api/admin/**").hasRole("ADMIN")
				                              .requestMatchers("/public/**").permitAll()
				                              .anyRequest().authenticated());
		http.csrf(AbstractHttpConfigurer::disable);
		// http.formLogin(withDefaults());
		http.httpBasic(withDefaults());
		return http.build();
	}
	
	@Bean
	public CommandLineRunner initData(RoleRepository roleRepository,
	                                  UserRepository userRepository,
	                                  PasswordEncoder passwordEncoder) {
		return args -> {
			Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
			                              .orElseGet(() ->
				                                         roleRepository.save(new Role(AppRole.ROLE_USER)));
			
			Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
			                               .orElseGet(() ->
				                                          roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
			
			if (!userRepository.existsByUserName("user1")) {
				User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
				user1.setAccountNonLocked(false);
				user1.setAccountNonExpired(true);
				user1.setCredentialsNonExpired(true);
				user1.setEnabled(true);
				user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
				user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
				user1.setTwoFactorEnabled(false);
				user1.setSignUpMethod("email");
				user1.setRole(userRole);
				userRepository.save(user1);
			}
			
			if (!userRepository.existsByUserName("admin")) {
				User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
				admin.setAccountNonLocked(true);
				admin.setAccountNonExpired(true);
				admin.setCredentialsNonExpired(true);
				admin.setEnabled(true);
				admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
				admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
				admin.setTwoFactorEnabled(false);
				admin.setSignUpMethod("email");
				admin.setRole(adminRole);
				userRepository.save(admin);
			}
		};
	}

//	@Bean
//	public UserDetailsService userDetailsService(DataSource datasource) {
//		JdbcUserDetailsManager manager = new JdbcUserDetailsManager(datasource);
//
//		if (!manager.userExists("user1")) {
//			manager.createUser(
//				User.withUsername("user1")
//				    .password("{noop}password1")
//				    .roles("USER")
//					.build()
//			);
//		}
//
//		if (!manager.userExists("admin")) {
//			manager.createUser(
//				new User.UserBuilder.userName("admin")
//				    .password("{noop}adminPass")
//				    .roles("ADMIN")
//					.build()
//			);
//		}
//
//		return manager;
//	}
}
