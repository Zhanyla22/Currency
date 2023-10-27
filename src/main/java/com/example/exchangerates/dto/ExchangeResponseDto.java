package com.example.exchangerates.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeResponseDto {

    String disclaimer;

    LocalDateTime timestamp;

    List<CurrencyResponseDto> rates;
}
