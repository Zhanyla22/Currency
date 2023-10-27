package com.example.exchangerates.controller;

import com.example.exchangerates.controller.base.BaseController;
import com.example.exchangerates.dto.ExchangeResponseDto;
import com.example.exchangerates.dto.ResponseDto;
import com.example.exchangerates.dto.TestDto;
import com.example.exchangerates.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
public class ExchangeController extends BaseController {

    private final ExchangeService exchangeService;

//    public ResponseEntity<ResponseDto> getAllExchange(){
//        return constructSuccessResponse();
//    }

//    public ResponseEntity<ResponseDto> addNewCurrency(){
//        return constructSuccessResponse();
//    }

//    @GetMapping("/get")
//    public ResponseEntity<ResponseDto> getCurrency() throws IOException {
//        return constructSuccessResponse(exchangeService.getCurrency());
//    }

    @GetMapping("/get")
    public ResponseEntity<ResponseDto> getById(@RequestParam String url) throws IOException {
        ExchangeResponseDto exchangeResponseDto = exchangeService.getCurrency(url);
        return constructSuccessResponse(exchangeResponseDto);
    }
}
