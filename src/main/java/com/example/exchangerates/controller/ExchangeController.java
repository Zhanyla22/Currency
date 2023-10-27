package com.example.exchangerates.controller;

import com.example.exchangerates.controller.base.BaseController;
import com.example.exchangerates.dto.ResponseDto;
import com.example.exchangerates.service.ExchangeService;
import com.example.exchangerates.service.impl.CurrencyUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/exchange")
public class ExchangeController extends BaseController {

    private final ExchangeService exchangeService;

    @GetMapping("/get-currency")
    public ResponseEntity<ResponseDto> get(){
        return constructSuccessResponse(exchangeService.getAll());
    }

    @GetMapping("/get-current")
    public ResponseEntity<ResponseDto> getCurrent(){
        return constructSuccessResponse(exchangeService.getCurrent());
    }

    @GetMapping("/get-by-date")
    public ResponseEntity<ResponseDto> getByDate(@RequestParam String date){
        return constructSuccessResponse(exchangeService.getByDate(date));
    }
}
