package com.example.crosspuzzleserver.util.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }

    public NotFoundException(HttpStatus httpStatus) {
        super(httpStatus);
    }
}
