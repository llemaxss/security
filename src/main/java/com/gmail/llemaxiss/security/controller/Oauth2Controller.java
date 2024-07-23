package com.gmail.llemaxiss.security.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/my-oauth2")
public class Oauth2Controller {
	@GetMapping("/")
	public String main(OAuth2AuthenticationToken token) {
		System.out.println(token.getPrincipal());

		return "oauth2.html";
	}
}
