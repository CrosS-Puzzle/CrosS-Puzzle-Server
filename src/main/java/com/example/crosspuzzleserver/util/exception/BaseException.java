package com.example.crosspuzzleserver.util.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class BaseException extends RuntimeException {

    HttpStatus httpStatus;
    String message;

    public BaseException(HttpStatus httpStatus, String message) {
        super();
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public BaseException(HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    public int getStatusCode() {
        return this.getHttpStatus().value();
    }
}
