package com.webapp.flightsearch.controller;

import com.amadeus.exceptions.ResponseException;
import com.webapp.flightsearch.service.FlightSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FlightControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightSearchService flightSearchService;

    private com.amadeus.resources.Location[] mockLocations;
    private com.amadeus.resources.FlightOfferSearch[] mockFlightsLONToNYC;

    @BeforeEach
    public void setup() throws ResponseException {
        com.amadeus.resources.Location mockLocationSHA = mock(com.amadeus.resources.Location.class);
        when(mockLocationSHA.getName()).thenReturn("HONGQIAO INTL");
        when(mockLocationSHA.getIataCode()).thenReturn("SHA");

        com.amadeus.resources.Location mockLocationPU = mock(com.amadeus.resources.Location.class);
        when(mockLocationPU.getName()).thenReturn("PUDONG INTL");
        when(mockLocationPU.getIataCode()).thenReturn("PU");

        mockLocations = new com.amadeus.resources.Location[]{mockLocationSHA, mockLocationPU};
        when(flightSearchService.location("CN")).thenReturn(mockLocations);

        com.amadeus.resources.FlightOfferSearch mockFlight = mock(
                com.amadeus.resources.FlightOfferSearch.class);
        mockFlightsLONToNYC = new com.amadeus.resources.FlightOfferSearch[]{mockFlight};
        when(flightSearchService.flights("LON", "NYC", "2024-11-15", "3", "1", "ECONOMY", "2024-11-18"))
                .thenReturn(mockFlightsLONToNYC);
    }

    @Test
    void whenCallingLocationAPIWithCountryCode_itsRespondingWithRightNumberOfAirportsAssiociated()
            throws Exception {
        mockMvc.perform(get("/api/locations")
                        .param("keyword", "CN")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // This will print the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void whenCallingFlightsAPIWithParameters_itsRespondingWithRightInfo() throws Exception {
        mockMvc.perform(get("/api/flights")
                        .param("origin", "LON")
                        .param("destination", "NYC")
                        .param("departDate", "2024-11-15")
                        .param("adults", "3")
                        .param("children", "1")
                        .param("travelClass", "ECONOMY")
                        .param("returnDate", "2024-11-18")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // This will print the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void healthCheck_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Health OK"));
    }
}
