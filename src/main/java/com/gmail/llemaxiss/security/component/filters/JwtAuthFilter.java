package com.gmail.llemaxiss.security.component.filters;

import com.gmail.llemaxiss.security.component.AuthServerProxy;
import com.gmail.llemaxiss.security.component.authTokens.UsernamePasswordAuthToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//@Component
public class JwtAuthFilter extends OncePerRequestFilter {
	@Value("${jwt.signing.key}")
	private String jwtSigningKey;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);

		SecretKey secretKey = Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(jwt)
			.getBody();

		String username = String.valueOf(claims.get(AuthServerProxy.USERNAME_HEADER));

		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("user");
		Authentication authentication = new UsernamePasswordAuthToken(username, null, List.of(grantedAuthority));

		SecurityContextHolder.getContext()
			.setAuthentication(authentication);

		filterChain.doFilter(request, response);
	}

	/**
	 * Отключает фильтр в случае, когда в урле ЕСТЬ указанные пути
	 * Чтобы заупстить процесс проверки jwt-токена
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		List<String> urlsToCheck = List.of(
			AuthServerProxy.LOGIN_URL,
			AuthServerProxy.AUTH_URL,
			AuthServerProxy.OTP_CHECK_URL
		);

		return urlsToCheck.contains(request.getServletPath());
	}
}
