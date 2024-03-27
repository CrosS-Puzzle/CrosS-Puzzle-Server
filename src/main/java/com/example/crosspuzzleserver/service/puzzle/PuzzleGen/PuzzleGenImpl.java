package com.example.crosspuzzleserver.service.puzzle.PuzzleGen;

import com.example.crosspuzzleserver.domain.AnswersInfo;
import com.example.crosspuzzleserver.domain.Category;
import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.repository.AnswersInfoRepository;
import com.example.crosspuzzleserver.repository.CategoryRepository;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import com.example.crosspuzzleserver.repository.QuestionInfoRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PuzzleGenImpl implements PuzzleGenService {

    private final WordPuzzle wordPuzzle;
    private final CategoryRepository categoryRepository;
    private final QuestionInfoRepository questionInfoRepository;
    private final AnswersInfoRepository answersInfoRepository;
    private final CrossWordsRepository crossWordsRepository;

    @Override
    @Scheduled(cron = "0 0 3 * * *")
    public boolean generatePuzzle() {

        List<ObjectId> randomCategoryId = getRandomCategoryIds();
        CrossWords crossWords = wordPuzzle.generateCrossWord(randomCategoryId);

        //중복체크
        //중복 체크 성공시 저장, 실패시 리트 or 종료
        if (isDuplicated(crossWords)) {
            log.info("duplicated puzzle");
            return false;
        }
        log.info("create puzzle success");
        answersInfoRepository.saveAll(crossWords.getAnswersInfo());
        questionInfoRepository.save(crossWords.getQuestionInfos());
        crossWordsRepository.save(crossWords);
        return true;
    }

    private boolean isDuplicated(CrossWords crossWords) {
        List<ObjectId> categoryIds = new ArrayList<>();
        for (Category c : crossWords.getCategories()) {
            categoryIds.add(c.getId());
        }
        //카테고리 아이디를 통해 전부 가져와서
        List<CrossWords> crossWordsList = crossWordsRepository.getCrossWordsByCategoryIds(categoryIds)
                .orElse(new ArrayList<>());
        //중복체크
        for (CrossWords cw : crossWordsList) {
            if (isDuplicatedWords(cw, crossWords)) {
                return true;
            }
        }
        return false;
    }

    private List<ObjectId> getRandomCategoryIds() {
        List<Category> categories = categoryRepository.findAll();

        int randomInt = (int) ((Math.random() * (categories.size())));
        ObjectId categoryId = categories.get(randomInt).getId();
        List<ObjectId> categoryIds = new ArrayList<>();
        categoryIds.add(categoryId);
        return categoryIds;
    }

    private boolean isDuplicatedWords(CrossWords cw, CrossWords origin) {
        Set<String> cwWordsSet = new HashSet<>(getWordsValueList(cw.getAnswersInfo()));
        Set<String> originWordsSet = new HashSet<>(getWordsValueList(origin.getAnswersInfo()));

        return cwWordsSet.size() == originWordsSet.size();
    }

    private List<String> getWordsValueList(List<AnswersInfo> answersInfoList) {
        List<String> wordsValueList = new ArrayList<>();
        for (AnswersInfo words : answersInfoList) {
            wordsValueList.add(words.getWords().getValue());
        }
        return wordsValueList;
    }
}


