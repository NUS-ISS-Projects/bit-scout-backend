package com.webapp.cryptotracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "com.webapp.cryptotracker")
public class CryptoPriceTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoPriceTrackingApplication.class, args);
    }
}
