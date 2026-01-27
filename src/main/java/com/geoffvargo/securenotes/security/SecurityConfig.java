package com.geoffvargo.securenotes.security;

import com.geoffvargo.securenotes.models.*;
import com.geoffvargo.securenotes.models.Role;
import com.geoffvargo.securenotes.repositories.*;
import com.geoffvargo.securenotes.security.jwt.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.*;
import org.springframework.security.config.annotation.method.configuration.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.*;
import org.springframework.security.web.csrf.*;

import java.time.*;

import static org.springframework.security.config.Customizer.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true,
                      securedEnabled = true,
                      jsr250Enabled = true)
public class SecurityConfig {
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf ->
			          csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			              .ignoringRequestMatchers("/api/auth/public/**"));
//		http.csrf(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests((requests)
			                           -> requests
//				                              .requestMatchers("/api/audit/**").hasRole("ADMIN")
				                              .requestMatchers("/api/admin/**").hasRole("ADMIN")
				                              .requestMatchers("/api/csrf-token").permitAll()
				                              .requestMatchers("/api/auth/public/**").permitAll()
				                              .anyRequest().authenticated());
		http.exceptionHandling(e -> e.authenticationEntryPoint(unauthorizedHandler));
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(new CustomLoggingFilter(), UsernamePasswordAuthenticationFilter.class);
		http.formLogin(withDefaults());
//		http.httpBasic(withDefaults());
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
}

