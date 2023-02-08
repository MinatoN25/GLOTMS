package com.glotms.authenticationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.glotms.authenticationservice.dto.JwtResponse;
import com.glotms.authenticationservice.model.User1;
import com.glotms.authenticationservice.util.AuthConstants;
import com.glotms.authenticationservice.util.JwtUtil;

@RestController
@RequestMapping(AuthConstants.ROOT_PATH)
public class UserController {

	private AuthenticationManager authenticationManager;
	private JwtUtil jwtUtil;

	@Autowired
	public UserController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping(AuthConstants.LOGIN_PATH)
	public ResponseEntity<?> loginUser(@RequestBody User1 user) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUserEmail(), user.getPassword()));

		return new ResponseEntity<>(
				new JwtResponse(jwtUtil.generateToken(user.getUserEmail()), "Bearer", user.getUserEmail()),
				HttpStatus.ACCEPTED);
	}

}
