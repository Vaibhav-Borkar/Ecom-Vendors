package com.ecom.service;

import com.ecom.model.User;

public interface UserService {
  
	User findUserByJwtToken(String token) throws Exception;
	
	User findUserByEmail(String email) throws Exception;
	
}
