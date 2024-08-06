package com.webapp.cryptotracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CryptoPriceFetcher {

    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd";
    private static final String PRICE_TOPIC = "crypto-prices";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 15000) // Polling every 15 seconds
    public void fetchPrices() {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Map<String, LinkedHashMap<String, Object>> prices = restTemplate.getForObject(COINGECKO_API_URL, Map.class);
            prices.forEach((cryptoId, priceMap) -> {
                priceMap.forEach((currency, value) -> {
                    BigDecimal priceValue = convertToBigDecimal(value);
                    kafkaTemplate.send(PRICE_TOPIC, cryptoId + ":" + priceValue);
                });
            });
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 429) {
                // Handle rate limit exceeded
                System.err.println("Rate limit exceeded: " + e.getResponseBodyAsString());
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if (value instanceof Integer) {
            return BigDecimal.valueOf((Integer) value);
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        } else if (value instanceof String) {
            return new BigDecimal((String) value);
        } else {
            throw new IllegalArgumentException("Unsupported value type");
        }
    }
}
