package com.example.exchangerates.controller;

import com.example.exchangerates.controller.base.BaseController;
import com.example.exchangerates.dto.ResponseDto;
import com.example.exchangerates.dto.TestDto;
import com.example.exchangerates.external.CurrencyUpdateService;
import com.example.exchangerates.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController extends BaseController {

    private final ExchangeService exchangeService;

    private final CurrencyUpdateService currencyUpdateService;

    @GetMapping("/get")
    public ResponseEntity<ResponseDto> getById(@RequestParam String url) throws IOException {
        List<TestDto> testDto = exchangeService.getAll(url);
        return constructSuccessResponse(testDto);
    }

    @GetMapping("/get-currency")
    public ResponseEntity<ResponseDto> get() throws Exception {
        return constructSuccessResponse(currencyUpdateService.getAll());
    }

    @GetMapping("/get-current")
    public ResponseEntity<ResponseDto> getCurrent() throws Exception {
        return constructSuccessResponse(currencyUpdateService.getCurrent());
    }

    @GetMapping("/get-by-date")
    public ResponseEntity<ResponseDto> getByDate(@RequestParam String date) throws Exception{
        return constructSuccessResponse(currencyUpdateService.getByDate(date));
    }
}
