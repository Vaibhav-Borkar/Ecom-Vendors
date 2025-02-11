package com.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecom.model.User;

public interface UserRepository extends JpaRepository<User,Long>{

	User findByEmail(String email);
}
