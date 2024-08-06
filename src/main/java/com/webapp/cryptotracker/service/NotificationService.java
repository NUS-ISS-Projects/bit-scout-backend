package com.webapp.cryptotracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.webapp.cryptotracker.entity.Notification;
import com.webapp.cryptotracker.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotification(String userId, String message) {
        Notification notification = new Notification(userId, message);
        notificationRepository.save(notification);
        kafkaTemplate.send("notifications", userId, message);
    }
}

