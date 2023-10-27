package com.example.exchangerates.exceptions;

import org.springframework.http.HttpStatus;

public class JsonParseException extends BaseException {

    public JsonParseException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
