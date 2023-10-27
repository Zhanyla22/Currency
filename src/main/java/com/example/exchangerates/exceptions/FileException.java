package com.example.exchangerates.exceptions;

import org.springframework.http.HttpStatus;

public class FileException extends BaseException {

    public FileException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
