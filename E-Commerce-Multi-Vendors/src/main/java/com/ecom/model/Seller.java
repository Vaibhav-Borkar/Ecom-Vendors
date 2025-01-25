package com.ecom.model;

import com.ecom.domain.AccountStatus;
import com.ecom.domain.USER_ROLE;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Seller {

	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private Long id;
	
	private String sellerName;
	
	private String mobile;
	
	@Column(unique = true,nullable = false)
	private String email;
	
	private String password;
	
	@Embedded
	private BusinessDetails businesDetails = new BusinessDetails();
	
	@Embedded
	private BankDetails bankDetails = new BankDetails();
	
	@OneToOne
	private Address pickupAdress =new Address();
	
	private String GSTIN;
	
	private USER_ROLE role;
	
	private boolean isEmailVerified=false;
	
	private AccountStatus accountStatus =  AccountStatus.PENDING_VERIFICATION;
}
