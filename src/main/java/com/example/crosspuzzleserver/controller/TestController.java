package com.example.crosspuzzleserver.controller;


import com.example.crosspuzzleserver.domain.AnswersInfo;
import com.example.crosspuzzleserver.domain.CrossPuzzle;
import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.repository.AnswersInfoRepository;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import com.example.crosspuzzleserver.repository.TestRepo;
import com.example.crosspuzzleserver.util.error.Error;
import com.example.crosspuzzleserver.util.exception.BadRequestException;
import com.example.crosspuzzleserver.util.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final TestRepo testRepo;
    private final AnswersInfoRepository answersInfoRepository;
    private final CrossWordsRepository crossWordsRepository;

    @GetMapping("/health")
    public String checkHealth() {
        return "OK";
    }

    @GetMapping("/testdb")
    public ResponseEntity<ApiResponse> testdb() {
        CrossPuzzle crossPuzzle = testRepo.findCrossPuzzleById("65956246b8817d0461363432");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(crossPuzzle));
    }

    @PostMapping("/postdb")
    public void postdb() {
        int[] arr = new int[]{1, 2};
        AnswersInfo answersInfo = AnswersInfo.builder()
                .coords(arr)
                .wordId("123")
                .direction("col")
                .build();

        answersInfoRepository.save(answersInfo);

        List<AnswersInfo> answersInfos = new ArrayList<>();
        answersInfos.add(answersInfo);

        CrossWords crossWords = CrossWords.builder()
                .answersInfo(answersInfos)
                .cateId("123")
                .build();

        crossWordsRepository.save(crossWords);


    }

    @GetMapping("/error")
    public ResponseEntity<ApiResponse> testError() {

        throw new BadRequestException(Error.BAD_REQUEST.getMessage());

    }


}
