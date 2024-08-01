package com.webapp.cryptotracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.webapp.cryptotracker.service.CryptoPriceService;
import com.webapp.cryptotracker.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@WebMvcTest(CryptoPriceController.class)
public class CryptoPriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoPriceService cryptoPriceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPrices() throws Exception {
        String ids = "bitcoin,ethereum";
        String vsCurrencies = "usd";

        Map<String, Object> expectedResponse = Map.of(
            "bitcoin", Map.of("usd", 45000),
            "ethereum", Map.of("usd", 3000)
        );

        when(cryptoPriceService.getPrices(ids, vsCurrencies)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/prices")
                .param("ids", ids)
                .param("vs_currencies", vsCurrencies))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bitcoin.usd").value(45000))
                .andExpect(jsonPath("$.ethereum.usd").value(3000));
    }

    @Test
    void getPricesWithMissingIdsParameter() throws Exception {
        String vsCurrencies = "usd";

        mockMvc.perform(get("/api/prices")
                .param("vs_currencies", vsCurrencies))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPricesWithMissingVsCurrenciesParameter() throws Exception {
        String ids = "bitcoin,ethereum";

        mockMvc.perform(get("/api/prices")
                .param("ids", ids))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPricesWithInvalidCurrency() throws Exception {
        String ids = "bitcoin";
        String vsCurrencies = "invalid_currency";

        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("invalid_currency", null);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("bitcoin", innerMap);

        when(cryptoPriceService.getPrices(ids, vsCurrencies)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/prices")
                .param("ids", ids)
                .param("vs_currencies", vsCurrencies))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bitcoin.invalid_currency").doesNotExist());
    }

    @Test
    void getPricesServiceThrowsException() throws Exception {
        String ids = "bitcoin";
        String vsCurrencies = "usd";

        when(cryptoPriceService.getPrices(ids, vsCurrencies)).thenThrow(new RuntimeException("Service Unavailable"));

        mockMvc.perform(get("/api/prices")
                .param("ids", ids)
                .param("vs_currencies", vsCurrencies))
                .andExpect(status().isInternalServerError());
    }
}
