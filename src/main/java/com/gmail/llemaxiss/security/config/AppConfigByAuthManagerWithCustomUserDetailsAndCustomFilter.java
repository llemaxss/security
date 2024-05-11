package com.gmail.llemaxiss.security.config;

import com.gmail.llemaxiss.security.components.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class AppConfigByAuthManagerWithCustomUserDetailsAndCustomFilter {
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
		UserDetailsService userDetailsService = userDetailsService();
		PasswordEncoder passwordEncoder = passwordEncoder();
		
		return httpSecurity
			.getSharedObject(AuthenticationManagerBuilder.class)
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder)
			.and()
			.build();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
			.httpBasic(Customizer.withDefaults())
			.addFilterBefore(new CustomFilter(), BasicAuthenticationFilter.class)
			.authorizeHttpRequests(auth -> {
				auth.requestMatchers("/hello/security").authenticated();
				auth.anyRequest().permitAll();
			})
			.build();
	}
	
	private UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
		
		UserDetails userDetails = User.withUserDetails(
			new CustomUserDetails("llemaxss", "123456")
		).build();
		
		userDetailsService.createUser(userDetails);
		
		return userDetailsService;
	}
	
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
}
