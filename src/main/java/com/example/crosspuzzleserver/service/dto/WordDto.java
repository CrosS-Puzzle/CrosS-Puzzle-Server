package com.example.crosspuzzleserver.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WordDto {

    private String id;
    private String value;
    private String description;
    private int length;
    private String category;

}
