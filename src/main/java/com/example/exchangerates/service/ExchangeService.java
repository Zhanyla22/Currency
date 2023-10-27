package com.example.exchangerates.service;

import com.example.exchangerates.dto.CombinedDto;

import java.util.List;

public interface ExchangeService {

    void currencyUpdate();

    List<CombinedDto> getAll();

    List<CombinedDto> getByDate(String date);

    List<CombinedDto> getCurrent();

}
