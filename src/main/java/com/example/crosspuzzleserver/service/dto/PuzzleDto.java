package com.example.crosspuzzleserver.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PuzzleDto {

    private String id;
    private int views;
    private int wins;
    private int rowSize;
    private int colSize;
    private List<AnswerInfoDto> answerInfoDtoList;

}
