package com.example.exchangerates.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Базовый exception, может быть выброшено в ходе выполнения программы.
 * Для создания пользовательских исключений, которые содержат сообщение об ошибке и HTTP статус
 *
 */
@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus status;

    /**
     *
     * @param message
     * принимает сообщение об ошибке
     * @param status
     * принимает статус HTTP
     */
    public BaseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
