package com.example.crosspuzzleserver.util.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum Error {

    //400
    BAD_REQUEST("잘못된 요청입니다."),
    ILLEGAL_SORT_REQUEST("잘못된 정렬 요청입니다."),

    //404
    NOT_FOUND_CATEGORY("존재하지 않는 카테고리 입니다."),
    NOT_FOUND_WORDS_BY_CATEGORY_NAME("카테고리에 해당하는 단어를 찾을 수 없습니다."),
    NOT_FOUND_PUZZLE("존재하지 않는 퍼즐 입니다.");

    private final String message;

}
