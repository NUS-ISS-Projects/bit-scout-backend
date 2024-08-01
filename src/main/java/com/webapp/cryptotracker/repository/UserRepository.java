package com.webapp.cryptotracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.cryptotracker.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserNameOrEmail(String username, String email);
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
    User findByUserName(String userName);
}