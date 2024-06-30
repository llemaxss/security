package com.gmail.llemaxiss.security.component.csrf;

import org.springframework.security.web.csrf.CsrfToken;

import java.util.UUID;

public class CustomCsrfToken implements CsrfToken {
	public static final String TOKEN_HEADER_NAME = "CUSTOM-CSRF-TOKEN";
	public static final String PARAMETER_NAME = "custom_csrf_";
	
	public static final String CLINT_ID_HEADER_NAME = "CUSTOM-CLIENT-ID";
	
	private UUID token;
	private String clientId;
	
	@Override
	public String getHeaderName() {
		/**
		 * By default = X-CSRF-TOKEN
		 */
		return TOKEN_HEADER_NAME;
	}
	
	@Override
	public String getParameterName() {
		/**
		 * By default = csrf_
		 */
		return PARAMETER_NAME;
	}
	
	@Override
	public String getToken() {
		return token.toString();
	}
	
	public void setToken(UUID token) {
		this.token = token;
	}
	
	public void setToken(String token) {
		this.token = UUID.fromString(token);
	}
	
	public String getClientId() {
		return clientId;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
}
