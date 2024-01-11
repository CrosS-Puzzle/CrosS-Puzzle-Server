package com.example.crosspuzzleserver.util.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum Error {

    //400
    BAD_REQUEST("잘못된 요청입니다."),


    //404
    NOT_FOUND_CATEGORY("존재하지 않는 카테고리 입니다.");

    private final String message;

}
