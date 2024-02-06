package com.example.crosspuzzleserver.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PuzzleListDto {

    List<String> categories;

    List<PuzzleDtoWithoutWords> puzzles;

    int currentPageNumber;
    int currentPageSize;

    int totalPage;
    long totalElement;

    String sorted;
    boolean isLast;

}
