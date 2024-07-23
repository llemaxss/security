package com.gmail.llemaxiss.security.config.ACBAMWCPOTAF;

import com.gmail.llemaxiss.security.component.providers.OtpAuthProvider;
import com.gmail.llemaxiss.security.component.providers.UsernamePasswordAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

//@Configuration
public class ACBAMWCPOTAF_2 {
	private UsernamePasswordAuthProvider usernamePasswordAuthProvider;
	private OtpAuthProvider otpAuthProvider;

	public ACBAMWCPOTAF_2(UsernamePasswordAuthProvider usernamePasswordAuthProvider, OtpAuthProvider otpAuthProvider) {
		this.usernamePasswordAuthProvider = usernamePasswordAuthProvider;
		this.otpAuthProvider = otpAuthProvider;
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
			.getSharedObject(AuthenticationManagerBuilder.class)
			.authenticationProvider(usernamePasswordAuthProvider)
			.authenticationProvider(otpAuthProvider)
			.build();
	}
}
