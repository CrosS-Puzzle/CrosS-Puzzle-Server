package com.example.crosspuzzleserver.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDto {

    private String id;

    private String name;

    private String koreanName;

    private long puzzleCount;
}
