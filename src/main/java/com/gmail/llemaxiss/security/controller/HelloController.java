package com.gmail.llemaxiss.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {
	@GetMapping()
	public ResponseEntity<String> helloGet() {
		return ResponseEntity.ok("Hello World!");
	}
	
	@PostMapping()
	public ResponseEntity<String> helloPost() {
		return ResponseEntity.ok("Hello World with POST!");
	}

	@GetMapping("/security")
	public ResponseEntity<String> helloSecurity() {
		return ResponseEntity.ok("Hello security World!");
	}
	
	@PostMapping("/ignore-csrf")
	public ResponseEntity<String> ignoreCsrf() {
		return ResponseEntity.ok("Hello ignored csrf World!");
	}
}
