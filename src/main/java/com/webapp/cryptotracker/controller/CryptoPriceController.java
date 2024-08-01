package com.webapp.cryptotracker.controller;

import com.webapp.cryptotracker.service.CryptoPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class CryptoPriceController {

    @Autowired
    private CryptoPriceService cryptoPriceService;

    @GetMapping
    public List<Map<String, Object>> getPrices(
        @RequestParam(name = "ids", required = true) String ids,
        @RequestParam(name = "vs_currency", required = true) String vsCurrency
    ) {
        return cryptoPriceService.getPrices(ids, vsCurrency);
    }
}
