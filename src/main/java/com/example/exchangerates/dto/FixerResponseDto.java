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
public class FixerResponseDto {

    boolean success;

    Literatura site;

    Long timestamp;

    Currency base;

    String date;

    Map<String, Double> rates;

    String localDate;
}
