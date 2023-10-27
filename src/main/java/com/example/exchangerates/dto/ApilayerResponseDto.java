package com.example.exchangerates.dto;

import com.example.exchangerates.enums.Currency;
import com.example.exchangerates.enums.Literatura;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApilayerResponseDto {

    boolean success;
    String terms;
    Literatura site;
    String privacy;
    Long timestamp;
    Currency source;
    Map<String, Double> quotes;
}
