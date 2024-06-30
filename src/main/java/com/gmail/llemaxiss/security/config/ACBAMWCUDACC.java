package com.gmail.llemaxiss.security.config;

import com.gmail.llemaxiss.security.component.CustomUserDetails;
import com.gmail.llemaxiss.security.component.csrf.CustomCsrfRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;

/**
 * AppConfigByAuthManagerWithCustomUserDetailsAndCustomCsrf
 */
//@EnableWebSecurity(debug = true)
//@Configuration
public class ACBAMWCUDACC {
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
		UserDetailsService userDetailsService = userDetailsService();
		PasswordEncoder passwordEncoder = passwordEncoder();
		
		/**
		 * Методы из настроек бина SecurityFilterChain здесь не работают не смотря на то, что их тут можно указать!
		 */
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
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/hello/security").authenticated()
					.anyRequest().permitAll()
			)
			.csrf(csrf -> csrf
					.csrfTokenRepository(getCsrfTokenRepository())
					.ignoringRequestMatchers("/hello/ignore-csrf")
			)
			.build();
	}
	
	
	public CsrfTokenRepository getCsrfTokenRepository() {
		return new CustomCsrfRepository();
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
