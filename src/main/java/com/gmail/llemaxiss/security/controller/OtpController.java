package com.gmail.llemaxiss.security.controller;

import com.gmail.llemaxiss.security.util.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/otp")
public class OtpController {
	private static final String VALID_USERNAME = "llemaxss";
	private static final String VALID_OTP_CODE = "OTP_CODE";

	@PostMapping("/check")
	public ResponseEntity<?> check(@RequestBody User user) {
		String username = user.getUsername();
		String code = user.getCode();

		if (VALID_USERNAME.equals(username) && VALID_OTP_CODE.equals(code)) {
			return ResponseEntity.ok()
				.build();
		} else {
			return ResponseEntity.badRequest()
				.build();
		}
	}
}
