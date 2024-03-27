package com.example.crosspuzzleserver.controller.puzzle;

import com.example.crosspuzzleserver.service.dto.CategoryDto;
import com.example.crosspuzzleserver.service.dto.CheckWordsDto;
import com.example.crosspuzzleserver.service.dto.IsAnswerDto;
import com.example.crosspuzzleserver.service.dto.puzzle.PuzzleDto;
import com.example.crosspuzzleserver.service.dto.puzzle.PuzzleListDto;
import com.example.crosspuzzleserver.service.puzzle.spi.Hits;
import com.example.crosspuzzleserver.service.spi.CategoryService;
import com.example.crosspuzzleserver.service.puzzle.spi.PuzzleService;
import com.example.crosspuzzleserver.service.spi.WordsService;
import com.example.crosspuzzleserver.util.cookie.CookieService;
import com.example.crosspuzzleserver.util.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("")
public class PuzzleQueryController {

    private final PuzzleService puzzleService;
    private final WordsService wordsService;
    private final CategoryService categoryService;
    private final CookieService cookieService;
    private final Hits hits;

    private final static String DEFAULT_PAGE = "0";
    private final static String DEFAULT_LIMIT = "12";
    private final static String DEFAULT_SORT = "asc";

    @GetMapping("")
    public ResponseEntity<ApiResponse> getPuzzleById(
            @RequestParam(value = "id", required = true) String id,
            @RequestParam(value = "answer", required = false, defaultValue = "false") String answer,
            @CookieValue(value = "userCookie", required = false) String cookieValue
    ) {
        log.info("@@ input cookie " + cookieValue);
        if (cookieValue == null) {
            cookieValue = cookieService.getCookie();
        }
        log.info("start request "+"@@" + cookieValue);
        if (!hits.isHit(cookieValue, id)) {
            log.info("@@ is not hit");
            puzzleService.updatePuzzleHits(id);
        }

        PuzzleDto puzzleDto = puzzleService.getPuzzleById(id, answer);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookieValue)
                .body(ApiResponse.success(puzzleDto));
    }


    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getPuzzleListByCategoryIds(
            @RequestParam(value = "category", required = false, defaultValue = "") List<String> categoryIds,
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(value = "limit", required = false, defaultValue = DEFAULT_LIMIT) int limit,
            @RequestParam(value = "sort", required = false, defaultValue = DEFAULT_SORT) String sort
    ) {
        PuzzleListDto puzzlePage = puzzleService.getPuzzlesByCategoryIds(categoryIds, page, limit, sort);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(puzzlePage));
    }


    @PostMapping("/check")
    public ResponseEntity<ApiResponse> checkWords(
            @RequestBody CheckWordsDto checkWordsDto) {
        boolean isAns = wordsService.checkWords(checkWordsDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        IsAnswerDto.builder()
                                .success(isAns)
                                .build()
                ));
    }


    @GetMapping("/categories")
    public ResponseEntity<ApiResponse> getCategories() {
        List<CategoryDto> categoryDtos = categoryService.getCategories();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(
                        categoryDtos
                ));
    }


}
