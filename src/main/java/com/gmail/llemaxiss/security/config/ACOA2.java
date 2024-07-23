package com.gmail.llemaxiss.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

/**
 * AppConfigOAuth2
 */
@EnableWebSecurity(debug = true)
@Configuration
public class ACOA2 {
	@Value("${oauth2.authServerId}")
	private String authServerId;

	@Value("${oauth2.clientId}")
	private String clientId;

	@Value("${oauth2.clientSecret}")
	private String clientSecret;

	@Value("${oauth2.authUri}")
	private String authUri;

	@Value("${oauth2.tokenUri}")
	private String tokenUri;

	@Value("${oauth2.userInfoUri}")
	private String userInfoUri;

	@Value("${oauth2.clientName}")
	private String clientName;

	@Value("${oauth2.redirectUri}")
	private String redirectUri;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		ClientRegistrationRepository crr = clientRegistrationRepository();

		/**
		 * Блок oauth2Login можно будет не указывать полностью,
		 * если в application.properties настроить определенные переменные
		 * (см. файл)
		 */
		return httpSecurity
			/**
			 * Метод oauth2Login() регистрирует фильтр OAuth2LoginAuthenticationFilter
			 */
			.oauth2Login(oauth -> oauth
				.clientRegistrationRepository(crr).permitAll()
			)
			.authorizeHttpRequests(auth -> auth
				.anyRequest().authenticated()
			)
			.build();
	}

	/**
	 * ClientRegistrationRepository работает аналогично как UserDetailsService.
	 * Можно указать несколько ClientRegistration, тогда
	 * фильтр авторизации пройдется по всем
	 */
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		ClientRegistration cr = clientRegistration();

		return new InMemoryClientRegistrationRepository(cr);
	}

	/**
	 * Вместо полной реализации своего ClientRegistration
	 * можете испоользовать метод githubClientRegistration()
	 */
	private ClientRegistration clientRegistration() {
		return ClientRegistration.withRegistrationId(authServerId)
			.clientId(clientId)
			.clientSecret(clientSecret)
			.scope(new String[] {"read:user"})
			.authorizationUri(authUri)
			.tokenUri(tokenUri)
			.userInfoUri(userInfoUri)
			.userNameAttributeName("id")
			.clientName(clientName)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.redirectUri(redirectUri)
			.build();
	}

	/**
	 * CommonOAuth2Provider содержит готовые настройки для некоторых серверов авторизаций
	 */
	private ClientRegistration githubClientRegistration() {
		return CommonOAuth2Provider.GITHUB.getBuilder(authServerId)
			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.clientId(clientId)
			.clientSecret(clientSecret)
			.clientName(clientName)
			.redirectUri(redirectUri)
			.build();
	}
}
