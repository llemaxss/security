package com.gmail.llemaxiss.security.component.providers;

import com.gmail.llemaxiss.security.component.AuthServerProxy;
import com.gmail.llemaxiss.security.component.authTokens.OtpAuthToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class OtpAuthProvider implements AuthenticationProvider {
    private AuthServerProxy authServerProxy;

    public OtpAuthProvider(AuthServerProxy authServerProxy) {
        this.authServerProxy = authServerProxy;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String code = String.valueOf(authentication.getCredentials());

        boolean result = authServerProxy.sendOtp(username, code);

        if (result) {
            return new OtpAuthToken(username, code);
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }

    /**
     * Указыает, какая реализация UsernamePasswordAuthenticationToken будет принята для данного провайдера
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthToken.class.isAssignableFrom(authentication);
    }
}
