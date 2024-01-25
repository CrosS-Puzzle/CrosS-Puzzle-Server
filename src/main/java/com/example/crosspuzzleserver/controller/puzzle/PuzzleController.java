package com.example.crosspuzzleserver.controller.puzzle;

import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.service.PuzzleServiceImpl;
import com.example.crosspuzzleserver.service.dto.PuzzleDto;
import com.example.crosspuzzleserver.service.spi.PuzzleService;
import com.example.crosspuzzleserver.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/puzzle")
public class PuzzleController {

    private final PuzzleService puzzleService;
    private final static String DEFAULT_PAGE = "1";
    private final static String DEFAULT_LIMIT = "12";
    private final static String DEFAULT_SORT = "asc";

    @GetMapping("")
    public ResponseEntity<ApiResponse> getPuzzleById(
            @RequestParam(value = "id", required = true) String id,
            @RequestParam(value = "answer", required = true) String answer
    ) {

        PuzzleDto puzzleDto = puzzleService.getPuzzleById(id, answer);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(puzzleDto));
    }


    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getPuzzleListByCategoryId(
            @RequestParam(value = "catId", required = false) String categoryId,
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) int limit,
            @RequestParam(value = "sort", required = false, defaultValue = DEFAULT_SORT) String sort
    ) {

        Page<CrossWords> puzzlePage = puzzleService.getPuzzlesByCategoryId(categoryId, page, limit, sort);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(puzzlePage));
    }

}
