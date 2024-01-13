package com.example.crosspuzzleserver.util.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }
}
