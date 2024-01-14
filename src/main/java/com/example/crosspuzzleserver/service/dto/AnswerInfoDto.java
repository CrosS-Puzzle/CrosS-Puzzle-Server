package com.example.crosspuzzleserver.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnswerInfoDto {

    private int[] coords;
    private String direction;
    private int length;
    private WordDto wordDto;

}
