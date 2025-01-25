package com.ecom.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(SellerException.class)
	public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException se,WebRequest req){
		ErrorDetails details = new ErrorDetails();
		details.setError(se.getMessage());
		details.setDetails(req.getDescription(false));
		details.setTimestamp(LocalDateTime.now());
		return new ResponseEntity<>(details,HttpStatus.BAD_REQUEST);
	}
}
