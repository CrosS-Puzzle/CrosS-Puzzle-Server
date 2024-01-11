package com.example.crosspuzzleserver.util.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public BadRequestException(HttpStatus httpStatus) {
        super(httpStatus);
    }
}
