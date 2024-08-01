package com.webapp.cryptotracker.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CryptoPriceService {

    private static final String COINGECKO_API_URL = "https://api.coingecko.com/api/v3/simple/price";
    private final RestTemplate restTemplate;

    public CryptoPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Object> getPrices(String ids, String vsCurrencies) {
        String url = String.format("%s?ids=%s&vs_currencies=%s", COINGECKO_API_URL, ids, vsCurrencies);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        return response.getBody();
    }
}
