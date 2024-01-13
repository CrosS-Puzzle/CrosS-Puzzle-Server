package com.example.crosspuzzleserver.util.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

    public BadRequestException( String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public BadRequestException(HttpStatus httpStatus) {
        super(HttpStatus.BAD_REQUEST);
    }
}
