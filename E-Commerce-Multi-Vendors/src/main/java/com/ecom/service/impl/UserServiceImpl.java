package com.ecom.service.impl;

import org.springframework.stereotype.Service;

import com.ecom.config.JwtProvider;
import com.ecom.model.User;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

	private final  UserRepository userRepository;
	private final JwtProvider jwtProvider;
	
	@Override
	public User findUserByJwtToken(String token) throws Exception {
		String email = jwtProvider.getEmailFromJwtToken(token);
		return this.findUserByEmail(email);
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if(user==null) {
			throw new Exception ("User not found with email"+email);
		}
		return user;
	}

}
