package com.webapp.cryptotracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.webapp.cryptotracker.service.CryptoPriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
        String vsCurrency = "usd";

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

        when(cryptoPriceService.getPrices(ids, vsCurrency)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/prices")
                .param("ids", ids)
                .param("vs_currency", vsCurrency))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("bitcoin"))
                .andExpect(jsonPath("$[0].name").value("Bitcoin"))
                .andExpect(jsonPath("$[0].current_price").value(45000))
                .andExpect(jsonPath("$[0].price_change_percentage_1h_in_currency").value(-0.5))
                .andExpect(jsonPath("$[0].price_change_percentage_24h").value(2.0))
                .andExpect(jsonPath("$[0].price_change_percentage_7d").value(10.0))
                .andExpect(jsonPath("$[0].market_cap").value(850000000000L))
                .andExpect(jsonPath("$[0].total_volume").value(35000000000L))
                .andExpect(jsonPath("$[0].circulating_supply").value(18600000L))
                .andExpect(jsonPath("$[1].id").value("ethereum"))
                .andExpect(jsonPath("$[1].name").value("Ethereum"))
                .andExpect(jsonPath("$[1].current_price").value(3000))
                .andExpect(jsonPath("$[1].price_change_percentage_1h_in_currency").value(-0.3))
                .andExpect(jsonPath("$[1].price_change_percentage_24h").value(1.5))
                .andExpect(jsonPath("$[1].price_change_percentage_7d").value(8.0))
                .andExpect(jsonPath("$[1].market_cap").value(350000000000L))
                .andExpect(jsonPath("$[1].total_volume").value(15000000000L))
                .andExpect(jsonPath("$[1].circulating_supply").value(115000000L));
    }

    @Test
    void getPricesWithMissingIdsParameter() throws Exception {
        String vsCurrency = "usd";

        mockMvc.perform(get("/api/prices")
                .param("vs_currency", vsCurrency))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPricesWithMissingVsCurrencyParameter() throws Exception {
        String ids = "bitcoin,ethereum";

        mockMvc.perform(get("/api/prices")
                .param("ids", ids))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPricesWithInvalidCurrency() throws Exception {
        String ids = "bitcoin";
        String vsCurrency = "invalid_currency";

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

        when(cryptoPriceService.getPrices(ids, vsCurrency)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/prices")
                .param("ids", ids)
                .param("vs_currency", vsCurrency))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("bitcoin"))
                .andExpect(jsonPath("$[0].current_price").doesNotExist())
                .andExpect(jsonPath("$[0].price_change_percentage_1h_in_currency").doesNotExist())
                .andExpect(jsonPath("$[0].price_change_percentage_24h").doesNotExist())
                .andExpect(jsonPath("$[0].price_change_percentage_7d").doesNotExist())
                .andExpect(jsonPath("$[0].market_cap").doesNotExist())
                .andExpect(jsonPath("$[0].total_volume").doesNotExist())
                .andExpect(jsonPath("$[0].circulating_supply").doesNotExist());
    }

    @Test
    void getPricesServiceThrowsException() throws Exception {
        String ids = "bitcoin";
        String vsCurrency = "usd";

        when(cryptoPriceService.getPrices(ids, vsCurrency)).thenThrow(new RuntimeException("Service Unavailable"));

        mockMvc.perform(get("/api/prices")
                .param("ids", ids)
                .param("vs_currency", vsCurrency))
                .andExpect(status().isInternalServerError());
    }
}
