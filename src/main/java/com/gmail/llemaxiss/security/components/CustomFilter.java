package com.gmail.llemaxiss.security.components;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class CustomFilter implements Filter {
	private String name;
	
	public CustomFilter(String name) {
		this.name = name;
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
		
		String testParam = httpServletRequest.getHeader("TEST_PARAM");
		if (!StringUtils.hasText(testParam)) {
			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		System.out.println("This message from filter: " + name);
		
		filterChain.doFilter(servletRequest, servletResponse);
	}
}
