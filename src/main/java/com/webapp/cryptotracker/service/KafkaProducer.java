package com.webapp.cryptotracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final String NOTIFICATION_TOPIC = "notifications";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNotificationMessage(String message) {
        kafkaTemplate.send(NOTIFICATION_TOPIC, message);
    }
}
