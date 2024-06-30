package com.gmail.llemaxiss.security.component.authTokens;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class OtpAuthToken extends UsernamePasswordAuthenticationToken {
    public OtpAuthToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public OtpAuthToken(Object principal, Object credentials,
        Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
