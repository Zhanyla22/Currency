package com.example.exchangerates;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeRates {

    public static void main(String[] args) {
        SpringApplication.run(ExchangeRates.class, args);
    }
}