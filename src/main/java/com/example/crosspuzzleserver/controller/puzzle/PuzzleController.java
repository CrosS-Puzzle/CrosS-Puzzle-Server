package com.example.crosspuzzleserver.controller.puzzle;


import com.example.crosspuzzleserver.service.spi.PuzzleService;
import com.example.crosspuzzleserver.util.error.Error;
import com.example.crosspuzzleserver.util.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("")
public class PuzzleController {

    private final PuzzleService puzzleService;

    @PostMapping("/complete")
    public ResponseEntity<ApiResponse> updatePuzzleSuccessCount(
            @RequestParam(value = "id") String puzzleId
    ) {

        boolean isSuccess = puzzleService.updatePuzzleSuccessCount(puzzleId);
        if (isSuccess) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("success"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(
                        400, Error.FAIL_UPDATE_WIN_COUNT.getMessage()
                ));
    }

}