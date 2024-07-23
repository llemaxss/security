package com.gmail.llemaxiss.security.component.filters;

import com.gmail.llemaxiss.security.component.AuthServerProxy;
import com.gmail.llemaxiss.security.component.authTokens.OtpAuthToken;
import com.gmail.llemaxiss.security.component.authTokens.UsernamePasswordAuthToken;
import com.gmail.llemaxiss.security.component.providers.OtpAuthProvider;
import com.gmail.llemaxiss.security.component.providers.UsernamePasswordAuthProvider;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

//@Component
public class InitialAuthFilter extends OncePerRequestFilter {
    private AuthenticationManager authenticationManager;

    /**
     * Используется для подписания jwt токена
     * В данном случае используется один ключ для всех пользваоетлей.
     * Но можно (и лучше) задать для каждого пользваотеля свой секертный ключ,
     * что обезопасит всех и сделает удобным процесс инвалидации всех токенов
     * конкретного пользователя, путем простого изменения его секретного ключа
     */
    @Value("${jwt.signing.key}")
    private String jwtSigningKey;

    public InitialAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * При первом запросе (без otp кода) выполняется авторизация на основе {@link UsernamePasswordAuthProvider}
     * Далее, когда код пришел, то вызывается авторизация на основе {@link OtpAuthProvider}
     * и проставляется в заголовок jwt-токен
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader(AuthServerProxy.USERNAME_HEADER);
        String password = request.getHeader(AuthServerProxy.PASSWORD_HEADER);
        String otpCode = request.getHeader(AuthServerProxy.OTP_CODE_HEADER);

        if (StringUtils.hasText(otpCode)) {
            authByOtp(username, otpCode);

            String jwt = getJwt(username);
            response.setHeader(HttpHeaders.AUTHORIZATION, jwt);
        } else {
            authByUsernamePassword(username, password);
        }
    }

    /**
     * Отключает фильтр в случае, когда в урле НЕТ указанных путей
     * Чтобы каждый юзер смог зайти по данному пути
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        List<String> urlsToCheck = List.of(
            AuthServerProxy.LOGIN_URL
        );

        return !urlsToCheck.contains(request.getServletPath());
    }

    private String getJwt(String username) {
        SecretKey secretKey = Keys.hmacShaKeyFor(jwtSigningKey.getBytes(StandardCharsets.UTF_8));

        String jwt = Jwts.builder()
            .setClaims(Map.of(AuthServerProxy.USERNAME_HEADER, username))
            .signWith(secretKey)
            .compact();

        System.out.println(String.format("\nJWT for username{%s}: %s\n", username, jwt));
        return jwt;
    }

    /**
     * По наследнику UsernamePasswordAuthenticationToken определяется какой провайдер будет вызван
     * @see com.gmail.llemaxiss.security.component.providers.OtpAuthProvider#supports(Class)
     */
    private void authByOtp(String username, String code) {
        Authentication authentication = new OtpAuthToken(username, code);
        authenticationManager.authenticate(authentication);
    }

    /**
     * По наследнику UsernamePasswordAuthenticationToken определяется какой провайдер будет вызван
     * @see com.gmail.llemaxiss.security.component.providers.UsernamePasswordAuthProvider#supports(Class)
     */
    private void authByUsernamePassword(String username, String password) {
        Authentication authentication = new UsernamePasswordAuthToken(username, password);
        authenticationManager.authenticate(authentication);
    }
}
