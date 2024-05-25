package com.gmail.llemaxiss.security.config;

import com.gmail.llemaxiss.security.components.CustomFilter;
import com.gmail.llemaxiss.security.components.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * AppConfigByAuthManagerWithCustomUserDetailsAndCustomFilter
 */
//@EnableWebSecurity(debug = true)
//@Configuration
public class ACBAMWCUDACF {
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
			/**
			 * Метод можно расположить после .httpBasic() - разницы нет.
			 *
			 * Если использовать метод .addFilterAt(), то фильтр ДОБАВИТСЯ в очередь с тем же значением order,
			 * НО, никто не гарантирует порядок выполнения фильтров с одинаковым order!
			 *
			 * В методах добавления фильтра, вторым параметром указывается другой фильтр,
			 * НО он в свою очередь не обязан быть в цепочке фильтров
			 */
			.addFilterBefore(new CustomFilter("CustomFilter BEFORE"), BasicAuthenticationFilter.class)
			.httpBasic(Customizer.withDefaults())
				.addFilterAt(new CustomFilter("CustomFilter AT"), BasicAuthenticationFilter.class)
			.addFilterAfter(new CustomFilter("CustomFilter AFTER"), BasicAuthenticationFilter.class)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/hello/security").authenticated()
				.anyRequest().permitAll()
			)
			.csrf(csrf -> csrf
				.ignoringRequestMatchers("/hello/ignore-csrf")
			)
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
