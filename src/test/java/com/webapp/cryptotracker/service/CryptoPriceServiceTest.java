package com.webapp.cryptotracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
        String vsCurrencies = "usd";
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin,ethereum&vs_currencies=usd";

        Map<String, Object> expectedResponse = Map.of(
            "bitcoin", Map.of("usd", 45000),
            "ethereum", Map.of("usd", 3000)
        );

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}))
            .thenReturn(responseEntity);

        Map<String, Object> actualResponse = cryptoPriceService.getPrices(ids, vsCurrencies);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPricesWithDifferentValues() {
        String ids = "dogecoin";
        String vsCurrencies = "usd";
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=dogecoin&vs_currencies=usd";

        Map<String, Object> expectedResponse = Map.of(
            "dogecoin", Map.of("usd", 0.25)
        );

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}))
            .thenReturn(responseEntity);

        Map<String, Object> actualResponse = cryptoPriceService.getPrices(ids, vsCurrencies);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPricesWithInvalidCurrency() {
        String ids = "bitcoin";
        String vsCurrencies = "invalid_currency";
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=invalid_currency";

        // Use HashMap to allow null values
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("invalid_currency", null);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("bitcoin", innerMap);

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}))
            .thenReturn(responseEntity);

        Map<String, Object> actualResponse = cryptoPriceService.getPrices(ids, vsCurrencies);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPricesWithEmptyResponse() {
        String ids = "unknown_coin";
        String vsCurrencies = "usd";
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=unknown_coin&vs_currencies=usd";

        Map<String, Object> expectedResponse = Map.of();

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}))
            .thenReturn(responseEntity);

        Map<String, Object> actualResponse = cryptoPriceService.getPrices(ids, vsCurrencies);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void getPricesThrowsException() {
        String ids = "bitcoin";
        String vsCurrencies = "usd";
        String url = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";

        when(restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {}))
            .thenThrow(new RestClientException("Service Unavailable"));

        assertThrows(RestClientException.class, () -> {
            cryptoPriceService.getPrices(ids, vsCurrencies);
        });
    }
}
