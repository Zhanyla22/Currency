package com.example.exchangerates.dto;

import com.example.exchangerates.enums.Currency;
import com.example.exchangerates.enums.Literatura;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeRatesResponseDto {

    String disclaimer;

    String license;

    Literatura site;

    Long timestamp;

    Currency base;

    Map<String, Double> rates;

    String localDate;
}