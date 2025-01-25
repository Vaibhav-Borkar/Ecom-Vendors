package com.ecom.service;

import org.springframework.stereotype.Service;

import com.ecom.domain.USER_ROLE;
import com.ecom.request.LoginRequest;
import com.ecom.response.AuthResponse;
import com.ecom.response.SignupRequest;

@Service
public interface AuthService {
    
	String createUser(SignupRequest req) throws Exception;
	
	void sendLoginOtp(String email,USER_ROLE role) throws Exception;
	
	AuthResponse signing(LoginRequest req);
}
