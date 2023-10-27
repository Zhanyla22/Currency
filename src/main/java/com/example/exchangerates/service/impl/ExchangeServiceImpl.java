package com.example.exchangerates.service.impl;

import com.example.exchangerates.dto.ExchangeResponseDto;
import com.example.exchangerates.dto.TestDto;
import com.example.exchangerates.service.ExchangeService;
import com.example.exchangerates.util.ConverUtil;
import com.example.exchangerates.util.RequestUtil;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Value("${api.fixer.io}")
    private static String FIXER;

    OkHttpClient client = new OkHttpClient();
//
//    @Scheduled(fixedRateString = "${exchange.repeateInMS}")
//    @Override
//    public void addNewCurrency() {
//
//    }
//
    @Override
    public ExchangeResponseDto getCurrency(String url) throws IOException {
        try (Response response = client.newCall(RequestUtil.getRequest(url)).execute()){
            return ConverUtil.toObject(response.body().string(), ExchangeResponseDto.class);
        }
    }

    @Override
    public List<TestDto> getAll(String url) throws IOException {
        try (Response response = client.newCall(RequestUtil.getRequest(url)).execute()) {
            return ConverUtil.toObjects(response.body().string(), TestDto.class);
        }
    }

}
