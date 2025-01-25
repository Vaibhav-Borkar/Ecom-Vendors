package com.ecom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.model.User;
import com.ecom.response.AuthResponse;
import com.ecom.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	
	@GetMapping("/users/profile")
	public ResponseEntity<User> createUserHandler(
			@RequestHeader ("Authorization") String jwt
			) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		return ResponseEntity.ok(user);
	}
}
