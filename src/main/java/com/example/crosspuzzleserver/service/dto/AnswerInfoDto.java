package com.example.crosspuzzleserver.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnswerInfoDto {

    private int[] coords;
    private int direction;
    private int length;
    private WordDto word;

}
