package com.webapp.cryptotracker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private WatchlistService watchlistService;

    @KafkaListener(topics = "crypto-prices", groupId = "crypto-group")
    public void consumePrice(String message) {
        // Process the price update and check for significant changes
        boolean significantChange = checkForSignificantChange(message);
        System.out.println("KafkaConsumerService received: ");
        System.out.println(message);
        if (significantChange) {
            // Handle the watchlist for price changes
            handleWatchlist(message);
        }
    }

    private boolean checkForSignificantChange(String message) {
        // TODO: Implement logic to check for significant price changes
        return true; // Placeholder
    }

    private void handleWatchlist(String message) {
        // Example message format: "cryptoId:priceValue"
        String[] parts = message.split(":");
        if (parts.length != 2) {
            return; // Invalid message format
        }

        String cryptoId = parts[0];
        String priceValue = parts[1];
        
        // Assuming we have a list of userIds to check, this should come from somewhere
        List<String> userIds = getUserIds(); // Placeholder method

        for (String userId : userIds) {
            if (watchlistService.isInWatchlist(userId, cryptoId)) {
                // Notify the user about the change
                kafkaProducer.sendNotificationMessage("Watchlist update for user " + userId + ": " + message);
            }
        }
    }

    private List<String> getUserIds() {
        // TODO: Implement a method to fetch user IDs from the database or another service
        return new ArrayList<>(); // Placeholder
    }
}
