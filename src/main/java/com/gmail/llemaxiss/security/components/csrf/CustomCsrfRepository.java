package com.gmail.llemaxiss.security.components.csrf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class CustomCsrfRepository implements CsrfTokenRepository {
	private Set<CustomCsrfToken> tokens = new HashSet<>();
	
	@Override
	public CsrfToken generateToken(HttpServletRequest request) {
		CustomCsrfToken customCsrfToken = new CustomCsrfToken();
		
		customCsrfToken.setToken(UUID.randomUUID());
		
		System.out.println("Csrf token: " + customCsrfToken.getToken());
		return customCsrfToken;
	}
	
	@Override
	public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
		String clientId = request.getHeader(CustomCsrfToken.CLINT_ID_HEADER_NAME);
		
		Optional<CustomCsrfToken> tokenOptional = findTokenByClientId(clientId);
		
		if (tokenOptional.isPresent()) {
			CustomCsrfToken csrfToken = tokenOptional.get();
			csrfToken.setToken(token.getToken());
		} else {
			CustomCsrfToken customCsrfToken = new CustomCsrfToken();
			
			customCsrfToken.setToken(token.getToken());
			customCsrfToken.setClientId(clientId);
			
			tokens.add(customCsrfToken);
		}
	}
	
	@Override
	public CsrfToken loadToken(HttpServletRequest request) {
		String clientId = request.getHeader(CustomCsrfToken.CLINT_ID_HEADER_NAME);
		
		Optional<CustomCsrfToken> tokenOptional = findTokenByClientId(clientId);
		
		if (tokenOptional.isPresent()) {
			CustomCsrfToken customCsrfToken = new CustomCsrfToken();
			
			customCsrfToken.setClientId(clientId);
			customCsrfToken.setToken(tokenOptional.get().getToken());
			
			return customCsrfToken;
		}
		
		return null;
	}
	
	private Optional<CustomCsrfToken> findTokenByClientId(String clientId) {
		return tokens.stream()
			.filter(token -> token.getClientId().equals(clientId))
			.findFirst();
	}
}
