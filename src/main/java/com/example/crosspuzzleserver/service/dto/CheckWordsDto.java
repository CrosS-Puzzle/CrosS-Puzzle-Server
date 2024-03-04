package com.example.crosspuzzleserver.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckWordsDto {

    String id;
    String input;

}
