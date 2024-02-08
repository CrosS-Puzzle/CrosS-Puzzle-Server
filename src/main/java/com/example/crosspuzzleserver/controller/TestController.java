package com.example.crosspuzzleserver.controller;


import com.example.crosspuzzleserver.domain.Category;
import com.example.crosspuzzleserver.domain.Words;
import com.example.crosspuzzleserver.repository.AnswersInfoRepository;
import com.example.crosspuzzleserver.repository.CategoryRepository;
import com.example.crosspuzzleserver.repository.CrossWordsCustomQuery;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import com.example.crosspuzzleserver.repository.QuestionInfoRepository;
import com.example.crosspuzzleserver.repository.WordsRepository;
import com.example.crosspuzzleserver.service.WordPuzzle;
import com.example.crosspuzzleserver.util.error.Error;
import com.example.crosspuzzleserver.util.exception.BadRequestException;
import com.example.crosspuzzleserver.util.response.ApiResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private final CategoryRepository categoryRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/health")
    public String checkHealth() {
        return "OK";
    }

    @PostMapping("/postdb")
    public void postdb() {
        int[] arr = new int[]{1, 2};

//        Words words = mongoTemplate.findById(new ObjectId("65ae1dfe796e1899d36c89fc"), Words.class, "words");

        Category category = Category.builder()
                .name("OS")
                .koreanName("운영체제")
                .build();
        categoryRepository.save(category);

        String[] strings = new String[]{"기차", "차돌박이", "돌잡이", "이쑤시개", "포세이돈", "개선문", "선물포장"};

        List<Words> words = Arrays.stream(strings).map(str -> Words.builder()
                .value(str)
                .category(category)
                .description(str)
                .build()).collect(Collectors.toList());

        wordsRepository.saveAll(words);
        Category category1 = categoryRepository.findAll().get(0);

        List<ObjectId> objectIds = new ArrayList<>();
        objectIds.add(category1.getId());
        wordPuzzle.generateCrossWord(objectIds);
//
//        AnswersInfo answersInfo = AnswersInfo.builder()
//                .coords(arr)
//                .direction(1)
//                .words(words)
//                .build();
//        answersInfoRepository.save(answersInfo);
//
//        List<AnswersInfo> answersInfos = new ArrayList<>();
//        answersInfos.add(answersInfo);
//
//        List<Category> categories = new ArrayList<>();
//        categories.add(category);
//
//        QuestionInfos questionInfos = QuestionInfos.builder()
//                .winCount(0)
//                .viewCount(0)
//                .build();
//        questionInfoRepository.save(questionInfos);
//
//        CrossWords crossWords = CrossWords.builder()
//                .answersInfo(answersInfos)
//                .categories(categories)
//                .questionInfos(questionInfos)
//                .build();
//
//        crossWordsRepository.save(crossWords);
    }

    @GetMapping("/error")
    public ResponseEntity<ApiResponse> testError() {

        throw new BadRequestException(Error.BAD_REQUEST.getMessage());

    }

    @GetMapping("/testGen")
    public String testGen() {

        List<ObjectId> categories = new ArrayList<>();
        categories.add(categoryRepository.findAll().get(0).getId());
        wordPuzzle.generateCrossWord(categories);

        return "success";
    }


}
