package com.webapp.cryptotracker.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Notification {

    public Notification(String userId2, String message2) {
        //TODO Auto-generated constructor stub
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String message;
    private LocalDateTime timestamp;

    // Constructors, Getters, and Setters
}

