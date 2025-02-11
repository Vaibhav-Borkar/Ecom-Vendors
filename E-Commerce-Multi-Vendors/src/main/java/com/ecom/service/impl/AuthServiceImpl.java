package com.ecom.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecom.config.JwtProvider;
import com.ecom.domain.USER_ROLE;
import com.ecom.model.Cart;
import com.ecom.model.Seller;
import com.ecom.model.User;
import com.ecom.model.VerificationCode;
import com.ecom.repository.CartRepository;
import com.ecom.repository.SellerRepository;
import com.ecom.repository.UserRepository;
import com.ecom.repository.VerificationCodeRepository;
import com.ecom.request.LoginRequest;
import com.ecom.response.AuthResponse;
import com.ecom.response.SignupRequest;
import com.ecom.service.AuthService;
import com.ecom.service.EmailService;
import com.ecom.utils.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final JwtProvider jwtProvider;
	private final VerificationCodeRepository verificationCodeRepository;
	private final EmailService emailService;
	private final CustomUserServiceImpl customUserServiceImpl;
	private final SellerRepository sellerRepository;
	
	@Override
	public String createUser(SignupRequest req) throws Exception {
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());
		
		if(verificationCode==null || !verificationCode.getOtp().equals(req.getOtp())) {
			throw new Exception("Wrong otp..");
		}
		
		User user = userRepository.findByEmail(req.getEmail());
		if(user==null) {
			User createdUser = new User();
			createdUser.setEmail(req.getEmail());
			createdUser.setFullName(req.getFullName());
			createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
			createdUser.setMobile("3435343433");
			createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
			user=userRepository.save(createdUser);
			
			Cart cart = new Cart();
			cart.setUser(user);
			cartRepository.save(cart);
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return jwtProvider.generateToken(authentication);
	}


	@Override
	public void sendLoginOtp(String email,USER_ROLE role) throws Exception {
		String SIGNING_PREFIX="signing_";
//		String SELLER_PREFIX="seller_";
		
		
		if(email.startsWith(SIGNING_PREFIX)) {
			email.substring(SIGNING_PREFIX.length());
			if(role.equals(USER_ROLE.ROLE_SELLER)) {
				
				Seller seller = sellerRepository.findByEmail(email);
				if(seller==null) {
					throw new Exception("seller not found");
				}
			}
			else {
				User user = userRepository.findByEmail(email);
				if(user==null) {
					throw new Exception("User not found with email "+email+"");
				}
			}
		}
		VerificationCode isExists = verificationCodeRepository.findByEmail(email);
		if(isExists!=null) {
			verificationCodeRepository.delete(isExists);
		}
		
		String otp=OtpUtil.generateOtp();
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(email);
		verificationCodeRepository.save(verificationCode);
		
		String subject =" Your OTP for Account Verification ";
		String text ="Dear User,\r\n"
				+ "\r\n"
				+ "Thank you for using our service! We have received a request to verify your account. To complete the process, please use the One-Time Password (OTP) below:\r\n"
				+ "Your OTP: "+otp
				+ "\r\n"
				+ "This OTP is valid for the next 10 minutes. Please use it to complete your verification."
				+ "\r\n"
				+ "If you did not request this OTP, please disregard this email.\r\n"
				+ "\r\n"
				+ "Best regards,\r\n"
				+ "Instashop \r\n"
				+ "8945670892\r\n"
				+ "www.instashop.com";
		
		emailService.sendVerificationOtpEmails(email, otp, subject, text);
		
		
	}


	@Override
	public AuthResponse signing(LoginRequest req) {
		String username=req.getEmail();
		String otp= req.getOtp();
		Authentication authentication = authenticate(username,otp);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login success");
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
		
		authResponse.setRole(USER_ROLE.valueOf(roleName));
		
		return authResponse;
	}


	private Authentication authenticate(String username, String otp) {
		UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
		
		String SELLER_PREFIX="seller_";
		if(username.startsWith(SELLER_PREFIX)) {
			username=username.substring(SELLER_PREFIX.length());
		}
		
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid username or password");
		}
		
		VerificationCode verificationCode =verificationCodeRepository.findByEmail(username);
		if(verificationCode==null || !verificationCode.getOtp().equals(otp)) {
			throw new BadCredentialsException("wrong otp");
		}
		return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	}

}
