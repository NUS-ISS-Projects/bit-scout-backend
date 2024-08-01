package com.webapp.cryptotracker.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CryptoPriceService {

    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/coins/markets";
    private final RestTemplate restTemplate;

    public CryptoPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Map<String, Object>> getPrices(String ids, String vsCurrency) {
        String url = String.format("%s?vs_currency=%s&ids=%s", COINGECKO_API_URL, vsCurrency, ids);
        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        return response.getBody();
    }
}
