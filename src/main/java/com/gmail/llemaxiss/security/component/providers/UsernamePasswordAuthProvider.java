package com.gmail.llemaxiss.security.component.providers;

import com.gmail.llemaxiss.security.component.AuthServerProxy;
import com.gmail.llemaxiss.security.component.authTokens.UsernamePasswordAuthToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class UsernamePasswordAuthProvider implements AuthenticationProvider {
    private AuthServerProxy authServerProxy;

    public UsernamePasswordAuthProvider(AuthServerProxy authServerProxy) {
        this.authServerProxy = authServerProxy;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        authServerProxy.sendAuth(username, password);

        return new UsernamePasswordAuthToken(username, password);
    }

    /**
     * Указыает, какая реализация UsernamePasswordAuthenticationToken будет принята для данного провайдера
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthToken.class.isAssignableFrom(authentication);
    }
}
