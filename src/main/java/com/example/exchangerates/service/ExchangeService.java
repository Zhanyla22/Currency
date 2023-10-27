package com.example.exchangerates.service;

import com.example.exchangerates.dto.ExchangeResponseDto;
import com.example.exchangerates.dto.TestDto;

import java.io.IOException;
import java.util.List;

public interface ExchangeService {

//    void addNewCurrency();

    ExchangeResponseDto getCurrency(String url) throws IOException;

    List<TestDto> getAll(String url) throws IOException;
}
