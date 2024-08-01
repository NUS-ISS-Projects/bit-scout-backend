package com.webapp.cryptotracker.controller;

import com.webapp.cryptotracker.service.CryptoPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/prices")
public class CryptoPriceController {

    @Autowired
    private CryptoPriceService cryptoPriceService;

    @GetMapping
    public Map<String, Object> getPrices(
        @RequestParam(name = "ids", required = true) String ids,
        @RequestParam(name = "vs_currencies", required = true) String vsCurrencies
    ) {
        return cryptoPriceService.getPrices(ids, vsCurrencies);
    }
}
