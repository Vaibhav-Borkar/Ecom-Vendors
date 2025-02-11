package com.ecom.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.config.JwtProvider;
import com.ecom.domain.AccountStatus;
import com.ecom.domain.USER_ROLE;
import com.ecom.exception.SellerException;
import com.ecom.model.Address;
import com.ecom.model.Seller;
import com.ecom.repository.AddressRepository;
import com.ecom.repository.SellerRepository;
import com.ecom.service.SellerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService{

	
	private final SellerRepository sellerRepository;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final AddressRepository addressRepository;
	// Creating new according to me for saving business details
	
	
	@Override
	public Seller getSellerProfile(String token) throws Exception {
		String email = jwtProvider.getEmailFromJwtToken(token);
		return this.getSellerByEmail(email);
	}

	@Override
	public Seller createSeller(Seller seller) throws Exception {
		Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
		if(sellerExist!=null) {
			throw new Exception("seller already exists with this email id");
		}
		Address savedAddress = addressRepository.save(seller.getPickupAdress());
		
		Seller newSeller = new Seller();
		newSeller.setEmail(seller.getEmail());
		newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
		newSeller.setSellerName(seller.getSellerName());
		newSeller.setPickupAdress(savedAddress);
		newSeller.setGSTIN(seller.getGSTIN());
		newSeller.setRole(USER_ROLE.ROLE_SELLER);
		newSeller.setMobile(seller.getMobile());
		newSeller.setBankDetails(seller.getBankDetails());
		newSeller.setBusinesDetails(seller.getBusinesDetails());
		
		return sellerRepository.save(newSeller);
//		return newSeller;
	}

	@Override   
	public Seller getSellerById(Long id) throws SellerException {
		return sellerRepository.findById(id).orElseThrow(()-> new SellerException("seller not found with id"+id));
	}

	@Override
	public Seller getSellerByEmail(String email) throws Exception {
		Seller seller = sellerRepository.findByEmail(email);
		if(seller==null) {
			throw new Exception("Seller not found");
		}
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(AccountStatus status) {
		return sellerRepository.findByAccountStatus(status);
	}

	@Override
	public Seller updateSeller(Long id, Seller seller) throws Exception {
		Seller existingSeller = this.getSellerById(id);
		if(seller.getSellerName()!=null) {
			existingSeller.setSellerName(seller.getSellerName());
		}
		if (seller.getMobile()!=null) {
			existingSeller.setMobile(seller.getMobile());
			
		}
		
		if (seller.getEmail()!=null) {
			existingSeller.setMobile(seller.getEmail());
		}
		if (seller.getBusinesDetails()!=null && seller.getBusinesDetails().getBusinessName()!=null) {
			existingSeller.getBusinesDetails().setBusinessName(seller.getBusinesDetails().getBusinessName());
		}
		if (seller.getBankDetails()!=null
				&& seller.getBankDetails().getAccountHolderName()!=null
				&& seller.getBankDetails().getAccountNumber()!=null
				&& seller.getBankDetails().getIfscCode()!=null
			
				) {
			existingSeller.getBankDetails().setAccountHolderName(seller.getBankDetails()
					.getAccountHolderName());
			existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails()
					.getAccountNumber());
			existingSeller.getBankDetails().setIfscCode(seller.getBankDetails()
					.getIfscCode());
		}
		
		if(seller.getPickupAdress()!=null
				&& seller.getPickupAdress().getAddress()!=null
				&& seller.getPickupAdress().getCity()!=null
				&& seller.getPickupAdress().getMobile()!=null
				&& seller.getPickupAdress().getState()!=null
				) {
			existingSeller.getPickupAdress().setAddress(seller.getPickupAdress().getAddress());
			existingSeller.getPickupAdress().setCity(seller.getPickupAdress().getCity());
			existingSeller.getPickupAdress().setState(seller.getPickupAdress().getState());
			existingSeller.getPickupAdress().setMobile(seller.getPickupAdress().getMobile());
			existingSeller.getPickupAdress().setPinCode(seller.getPickupAdress().getPinCode());			
		}
		if(seller.getGSTIN()!=null) {
			existingSeller.setGSTIN(seller.getGSTIN());
		}
		return sellerRepository.save(existingSeller);
	}

	@Override
	public void deleteSeller(Long id) throws Exception {
		Seller seller = getSellerById(id);
		sellerRepository.delete(seller);
	}

	@Override
	public Seller verifyEmail(String email, String otp) throws Exception {
		Seller seller = getSellerByEmail(email);
		seller.setEmailVerified(true);
		return sellerRepository.save(seller);
	}

	@Override
	public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
		Seller seller = getSellerById(sellerId);
		seller.setAccountStatus(status);;
		return sellerRepository.save(seller);
	}

}
