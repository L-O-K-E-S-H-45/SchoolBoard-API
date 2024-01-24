package com.school.sba.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {   // now spring security will ignore default securityConfig
	
	/**
	 * Authentication is a process of ensuring the user is valid and unique and 
	 * Authorization is the process of defining what exact resource the user 
	 * should be able to access and what should not be accessed.
	 */
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf->csrf.disable())   // disabling cross-site-request-forging
				.authorizeHttpRequests(auth->auth.requestMatchers("/**").permitAll()
//						.requestMatchers("/users/{userId}/schools").hasRole("ADMIN")
						.anyRequest().authenticated()) //(here v r doing changes)  authenticating all except users/register the input HttpRequests                       
				.formLogin(Customizer.withDefaults())   // using default formLogin -> It allows only HttpSession 
//				.authenticationProvider(authenticationProvider())
				.build();
	}
	
	/** we have to pass two beans for the Authentication provider i,e 1. UserDetails & 2. PasswordEncoder(BCrypt algorithm helps to 
	encrypt any data-> hashing range=1to41 recommended to keep within 12 (if 41 application becomes slow))
	*/
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}
	
//	@Bean
//	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//		return authenticationConfiguration.getAuthenticationManager();
//	}

}






