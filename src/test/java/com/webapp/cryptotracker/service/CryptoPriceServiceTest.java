package com.webapp.cryptotracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class CryptoPriceServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CryptoPriceService cryptoPriceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPrices() {
        String ids = "bitcoin,ethereum";
        String vsCurrency = "usd";
        String url = String.format("https://api.coingecko.com/api/v3/coins/markets?vs_currency=%s&ids=%s", vsCurrency, ids);

        List<Map<String, Object>> expectedResponse = List.of(
            Map.of(
                "id", "bitcoin",
                "name", "Bitcoin",
                "current_price", 45000,
                "price_change_percentage_1h_in_currency", -0.5,
                "price_change_percentage_24h", 2.0,
                "price_change_percentage_7d", 10.0,
                "market_cap", 850000000000L,
                "total_volume", 35000000000L,
                "circulating_supply", 18600000L
            ),
            Map.of(
                "id", "ethereum",
                "name", "Ethereum",
                "current_price", 3000,
                "price_change_percentage_1h_in_currency", -0.3,
                "price_change_percentage_24h", 1.5,
                "price_change_percentage_7d", 8.0,
                "market_cap", 350000000000L,
                "total_volume", 15000000000L,
                "circulating_supply", 115000000L
            )
        );

        ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {}))
            .thenReturn(responseEntity);

        List<Map<String, Object>> actualResponse = cryptoPriceService.getPrices(ids, vsCurrency);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPricesWithDifferentValues() {
        String ids = "dogecoin";
        String vsCurrency = "usd";
        String url = String.format("https://api.coingecko.com/api/v3/coins/markets?vs_currency=%s&ids=%s", vsCurrency, ids);

        List<Map<String, Object>> expectedResponse = List.of(
            Map.of(
                "id", "dogecoin",
                "name", "Dogecoin",
                "current_price", 0.25,
                "price_change_percentage_1h_in_currency", 0.1,
                "price_change_percentage_24h", 5.0,
                "price_change_percentage_7d", 20.0,
                "market_cap", 32000000000L,
                "total_volume", 2400000000L,
                "circulating_supply", 130000000000L
            )
        );

        ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {}))
            .thenReturn(responseEntity);

        List<Map<String, Object>> actualResponse = cryptoPriceService.getPrices(ids, vsCurrency);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPricesWithInvalidCurrency() {
        String ids = "bitcoin";
        String vsCurrency = "invalid_currency";
        String url = String.format("https://api.coingecko.com/api/v3/coins/markets?vs_currency=%s&ids=%s", vsCurrency, ids);

        // Using HashMap to handle null values
        Map<String, Object> bitcoinResponse = new HashMap<>();
        bitcoinResponse.put("id", "bitcoin");
        bitcoinResponse.put("name", "Bitcoin");
        bitcoinResponse.put("current_price", null);
        bitcoinResponse.put("price_change_percentage_1h_in_currency", null);
        bitcoinResponse.put("price_change_percentage_24h", null);
        bitcoinResponse.put("price_change_percentage_7d", null);
        bitcoinResponse.put("market_cap", null);
        bitcoinResponse.put("total_volume", null);
        bitcoinResponse.put("circulating_supply", null);

        List<Map<String, Object>> expectedResponse = List.of(bitcoinResponse);

        ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {}))
            .thenReturn(responseEntity);

        List<Map<String, Object>> actualResponse = cryptoPriceService.getPrices(ids, vsCurrency);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPricesWithEmptyResponse() {
        String ids = "unknown_coin";
        String vsCurrency = "usd";
        String url = String.format("https://api.coingecko.com/api/v3/coins/markets?vs_currency=%s&ids=%s", vsCurrency, ids);

        List<Map<String, Object>> expectedResponse = List.of();

        ResponseEntity<List<Map<String, Object>>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {}))
            .thenReturn(responseEntity);

        List<Map<String, Object>> actualResponse = cryptoPriceService.getPrices(ids, vsCurrency);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPricesThrowsException() {
        String ids = "bitcoin";
        String vsCurrency = "usd";
        String url = String.format("https://api.coingecko.com/api/v3/coins/markets?vs_currency=%s&ids=%s", vsCurrency, ids);

        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Map<String, Object>>>() {}))
            .thenThrow(new RestClientException("Service Unavailable"));

        assertThrows(RestClientException.class, () -> {
            cryptoPriceService.getPrices(ids, vsCurrency);
        });
    }
}
