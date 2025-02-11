package com.ecom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.domain.USER_ROLE;
import com.ecom.repository.UserRepository;
import com.ecom.request.LoginOtpRequest;
import com.ecom.request.LoginRequest;
import com.ecom.response.ApiResponse;
import com.ecom.response.AuthResponse;
import com.ecom.response.SignupRequest;
import com.ecom.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserRepository userRepository;
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception{
	    String jwt = authService.createUser(req);
	    AuthResponse res = new AuthResponse();
	    res.setJwt(jwt);
	    res.setMessage("User registered successfully");
	    res.setRole(USER_ROLE.ROLE_CUSTOMER);
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/sent/login-signup-otp")
	public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest req) throws Exception{
	    authService.sendLoginOtp(req.getEmail(),req.getRole());
	    ApiResponse res = new ApiResponse();
	    res.setMessage("otp sent successfully");
		return ResponseEntity.ok(res);
	}
	
	
	@PostMapping("/signing")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception{
	    AuthResponse authResponse = authService.signing(req);
		return ResponseEntity.ok(authResponse);
	}
	
}
