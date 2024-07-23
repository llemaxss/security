package com.gmail.llemaxiss.security.component;

import com.gmail.llemaxiss.security.util.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//@Component
public class AuthServerProxy {
    public static final String USERNAME_HEADER = "username";
    public static final String PASSWORD_HEADER = "password";
    public static final String OTP_CODE_HEADER = "code";

    public static final String LOGIN_URL = "/login";
    public static final String AUTH_URL = "/user/auth";
    public static final String OTP_CHECK_URL = "/otp/check";


    private final RestTemplate restTemplate;

    @Value("${auth.server.url}")
    private String authServerUrl;

    public AuthServerProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendAuth(String username, String password) {
        String url = authServerUrl + AUTH_URL;

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        postForEntity(url, user, Void.class);
    }

    public boolean sendOtp(String username, String code) {
        String url = authServerUrl + OTP_CHECK_URL;

        User user = new User();
        user.setUsername(username);
        user.setCode(code);

        ResponseEntity response = postForEntity(url, user, Void.class);

        return response.getStatusCode()
            .equals(HttpStatus.OK);
    }

    private <T> ResponseEntity<T> postForEntity(String url, User user, Class<T> respType) {
        HttpEntity httpEntity = new HttpEntity<>(user);
        return restTemplate.postForEntity(url, httpEntity, respType);
    }
}
