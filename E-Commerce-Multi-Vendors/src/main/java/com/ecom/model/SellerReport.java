package com.ecom.model;

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

public class SellerReport {

	@Id
	@GeneratedValue(strategy =GenerationType.AUTO)
	private Long id;
	
	@OneToOne
	private Seller seller;
	
	private Long totalEarning=0L;
	
	private Long totalSales=0L;
	
	private Long totalRefunds=0L;
	
	private Long totalTax=0L;
	
	private Long netEarning=0L;
	
	private Integer totalOrders=0;
	
	private Integer cancelOrders=0;
	
	private Integer totalTransactions=0;
	
	
	
	
}
