package com.example.crosspuzzleserver.util.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(Include.NON_NULL)
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private T data;

    public static ApiResponse success(Object data) {
        return ApiResponse.builder()
                .data(data)
                .build();
    }

    public static ApiResponse fail(int errorCode,String message) {
        return ApiResponse.builder()
                .code(errorCode)
                .message(message)
                .build();
    }

}
