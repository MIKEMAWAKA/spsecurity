package com.mike.springsecurityclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mike.springsecurityclient.entity.PasswordRestToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordRestToken,Long>{

    PasswordRestToken findByToken(String token);
    
}
