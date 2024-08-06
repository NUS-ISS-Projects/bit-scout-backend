package com.webapp.cryptotracker.controller;

import com.webapp.cryptotracker.service.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("/publish")
    public void publishMessage(@RequestParam("message") String message) {
        kafkaProducer.sendNotificationMessage(message);
    }
}
