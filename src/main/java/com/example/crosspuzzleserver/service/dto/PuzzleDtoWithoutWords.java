package com.example.crosspuzzleserver.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PuzzleDtoWithoutWords {

    private String id;
    private int views;
    private int wins;

}
