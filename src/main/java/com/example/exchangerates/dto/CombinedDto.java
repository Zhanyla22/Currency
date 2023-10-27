package com.example.exchangerates.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CombinedDto {

    List<ApilayerResponseDto> apilayerResponseDto;

    List<ExchangeRatesResponseDto> exchangeRatesResponseDto;

    List<FixerResponseDto> fixerResponseDtos;
}