package com.example.crosspuzzleserver.util.advice;

import com.example.crosspuzzleserver.util.exception.BaseException;
import com.example.crosspuzzleserver.util.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse> handleException(BaseException baseException) {
        return ResponseEntity
                .status(baseException.getHttpStatus())
                .body(ApiResponse.fail(baseException.getStatusCode(), baseException.getMessage()));
    }


}
