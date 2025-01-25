package com.ecom.service;

import java.util.List;

import com.ecom.domain.AccountStatus;
import com.ecom.exception.SellerException;
import com.ecom.model.Seller;

public interface SellerService {

	Seller getSellerProfile(String token) throws Exception;
	Seller createSeller(Seller seller) throws Exception;
	Seller getSellerById(Long id) throws SellerException;
	Seller getSellerByEmail(String email) throws Exception;
	List<Seller> getAllSellers(AccountStatus status);
	Seller updateSeller(Long id,Seller seller) throws Exception;
	void deleteSeller(Long id) throws Exception;
	Seller verifyEmail(String email,String otp) throws Exception;
	Seller updateSellerAccountStatus(Long sellerId,AccountStatus status) throws Exception;
	
	
	
	
	
	
}
