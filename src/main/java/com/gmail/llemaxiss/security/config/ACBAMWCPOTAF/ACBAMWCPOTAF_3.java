package com.gmail.llemaxiss.security.config.ACBAMWCPOTAF;

import com.gmail.llemaxiss.security.component.filters.InitialAuthFilter;
import com.gmail.llemaxiss.security.component.filters.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class ACBAMWCPOTAF_3 {
	private InitialAuthFilter initialAuthFilter;
	private JwtAuthFilter jwtAuthFilter;

	public ACBAMWCPOTAF_3(InitialAuthFilter initialAuthFilter, JwtAuthFilter jwtAuthFilter) {
		this.initialAuthFilter = initialAuthFilter;
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
			.csrf(AbstractHttpConfigurer::disable)
			.addFilterAt(initialAuthFilter, BasicAuthenticationFilter.class)
			.addFilterAfter(jwtAuthFilter, BasicAuthenticationFilter.class)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/otp/check").permitAll()
				.anyRequest().authenticated()
			)
			.build();
	}
}
