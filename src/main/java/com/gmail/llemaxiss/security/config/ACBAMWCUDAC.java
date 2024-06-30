package com.gmail.llemaxiss.security.config;

import com.gmail.llemaxiss.security.component.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * AppConfigByAuthManagerWithCustomUserDetailsAndCors
 */
//@EnableWebSecurity(debug = true)
//@Configuration
public class ACBAMWCUDAC {
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
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> {
				CorsConfigurationSource configurationSource = getCorsConfigurationSource();
				cors.configurationSource(configurationSource);
			})
			.build();
	}
	
	private CorsConfigurationSource getCorsConfigurationSource() {
		UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		
		corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080"));
		corsConfiguration.setAllowedMethods(List.of(HttpMethod.POST.name()));
		
		configurationSource.registerCorsConfiguration("/cors/config", corsConfiguration);
		
		return configurationSource;
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
