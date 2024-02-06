package com.example.crosspuzzleserver.controller;


import com.example.crosspuzzleserver.domain.AnswersInfo;
import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.domain.QuestionInfos;
import com.example.crosspuzzleserver.domain.Words;
import com.example.crosspuzzleserver.repository.AnswersInfoRepository;
import com.example.crosspuzzleserver.repository.CrossWordsCustomQuery;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import com.example.crosspuzzleserver.repository.QuestionInfoRepository;
import com.example.crosspuzzleserver.repository.WordsRepository;
import com.example.crosspuzzleserver.service.WordPuzzle;
import com.example.crosspuzzleserver.util.category.Category;
import com.example.crosspuzzleserver.util.error.Error;
import com.example.crosspuzzleserver.util.exception.BadRequestException;
import com.example.crosspuzzleserver.util.response.ApiResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final AnswersInfoRepository answersInfoRepository;
    private final CrossWordsRepository crossWordsRepository;
    private final WordPuzzle wordPuzzle;
    private final CrossWordsCustomQuery crossWordsCustomQuery;
    private final QuestionInfoRepository questionInfoRepository;
    private final WordsRepository wordsRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/health")
    public String checkHealth() {
        return "OK";
    }

    @PostMapping("/postdb")
    public void postdb() {
        int[] arr = new int[]{1, 2};

        Words words = mongoTemplate.findById(new ObjectId("65ae1dfe796e1899d36c89fc"), Words.class, "words");

        AnswersInfo answersInfo = AnswersInfo.builder()
                .coords(arr)
                .direction(1)
                .words(words)
                .build();

        answersInfoRepository.save(answersInfo);

        List<AnswersInfo> answersInfos = new ArrayList<>();
        answersInfos.add(answersInfo);

        List<String> categories = new ArrayList<>();
        categories.add("A");
        categories.add("B");

        QuestionInfos questionInfos = QuestionInfos.builder()
                .winCount(0)
                .viewCount(0)
                .build();
        questionInfoRepository.save(questionInfos);

        CrossWords crossWords = CrossWords.builder()
                .answersInfo(answersInfos)
                .categories(categories)
                .questionInfos(questionInfos)
                .build();

        crossWordsRepository.save(crossWords);

    }

    @GetMapping("/error")
    public ResponseEntity<ApiResponse> testError() {

        throw new BadRequestException(Error.BAD_REQUEST.getMessage());

    }

    @GetMapping("/testGen")
    public String testGen() {

        List<Category> categories = new ArrayList<>();
        categories.add(Category.OS);
        wordPuzzle.generateCrossWord(categories);

        return "success";
    }


}
