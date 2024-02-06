package com.example.crosspuzzleserver.service;

import com.example.crosspuzzleserver.domain.AnswersInfo;
import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.domain.QuestionInfos;
import com.example.crosspuzzleserver.domain.Words;
import com.example.crosspuzzleserver.repository.CrossWordsCustomQuery;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import com.example.crosspuzzleserver.repository.QuestionInfoRepository;
import com.example.crosspuzzleserver.service.dto.AnswerInfoDto;
import com.example.crosspuzzleserver.service.dto.PuzzleDto;
import com.example.crosspuzzleserver.service.dto.PuzzleDtoWithoutWords;
import com.example.crosspuzzleserver.service.dto.PuzzleListDto;
import com.example.crosspuzzleserver.service.dto.WordDto;
import com.example.crosspuzzleserver.service.spi.PuzzleService;
import com.example.crosspuzzleserver.util.error.Error;
import com.example.crosspuzzleserver.util.exception.BadRequestException;
import com.example.crosspuzzleserver.util.exception.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PuzzleServiceImpl implements PuzzleService {

    private final CrossWordsRepository crossWordsRepository;
    private final CrossWordsCustomQuery crossWordsCustomQuery;
    private final QuestionInfoRepository questionInfoRepository;

    @Transactional
    public PuzzleDto getPuzzleById(String puzzleId, String answer) {
        CrossWords crossWords = getCrossWordsById(puzzleId);

        if (!Boolean.parseBoolean(answer)) {
            updateViewCount(crossWords);
        }
        return crossWordsToPuzzleDto(crossWords, Boolean.parseBoolean(answer));
    }

    private CrossWords getCrossWordsById(String id) {
        return crossWordsRepository.findById(new ObjectId(id))
                .orElseThrow(() ->
                        new NotFoundException(Error.NOT_FOUND_PUZZLE.getMessage())
                );
    }

    private void updateViewCount(CrossWords crossWords) {
        QuestionInfos questionInfos = crossWords.getQuestionInfos();
        questionInfos.addViewCount();
        questionInfoRepository.save(questionInfos);
    }

    private PuzzleDto crossWordsToPuzzleDto(CrossWords crossWords, boolean includeValue) {
        return PuzzleDto.builder()
                .id(String.valueOf(crossWords.getId()))
                .views(crossWords.getQuestionInfos().getViewCount())
                .wins(crossWords.getQuestionInfos().getWinCount())
                .rowSize(crossWords.getRowSize())
                .colSize(crossWords.getColSize())
                .answerInfoDtoList(crossWords.getAnswersInfo().stream().map(
                        answersInfo -> getAnswerInfoDto(answersInfo, includeValue)
                ).collect(Collectors.toList()))
                .build();
    }

    private AnswerInfoDto getAnswerInfoDto(AnswersInfo answersInfo, boolean includeValue) {
        WordDto wordDto;
        if (includeValue) {
            wordDto = wordsToWordDtoWithValue(answersInfo.getWords());
        } else {
            wordDto = wordsToWordDtoWithOutValue(answersInfo.getWords());
        }

        return AnswerInfoDto.builder()
                .coords(answersInfo.getCoords())
                .length(answersInfo.getWords().getValue().length())
                .direction(answersInfo.getDirection())
                .wordDto(wordDto)
                .build();
    }


    private WordDto wordsToWordDtoWithValue(Words words) {
        return WordDto.builder()
                .category(words.getCategory())
                .id(String.valueOf(words.getId()))
                .value(words.getValue())
                .description(words.getDescription())
                .build();
    }

    private WordDto wordsToWordDtoWithOutValue(Words words) {
        return WordDto.builder()
                .category(words.getCategory())
                .id(String.valueOf(words.getId()))
                .description(words.getDescription())
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public PuzzleListDto getPuzzlesByCategoryName(List<String> categoryName, int page, int limit, String sort) {

        Direction direction = getDirection(sort);
        PageRequest pageRequest = PageRequest.of(page, limit, direction, "_id");

        Page<CrossWords> crossWordsPage = crossWordsCustomQuery.findByCategories(categoryName, pageRequest);

        return getPuzzleListDto(crossWordsPage);
    }

    private PuzzleListDto getPuzzleListDto(Page<CrossWords> crossWordsPage) {
        return PuzzleListDto.builder()
                .categories(crossWordsPage.getContent().get(0).getCategories())
                .puzzles(crossWordsPage.getContent().stream()
                        .map(this::getPuzzleDtoWithoutWords)
                        .toList())
                .currentPageNumber(crossWordsPage.getPageable().getPageNumber())
                .currentPageNumber(crossWordsPage.getPageable().getPageSize())
                .sorted(crossWordsPage.getSort().toString())
                .totalPage(crossWordsPage.getTotalPages())
                .totalElement(crossWordsPage.getTotalElements())
                .isLast(crossWordsPage.isLast())
                .build();
    }

    private PuzzleDtoWithoutWords getPuzzleDtoWithoutWords(CrossWords crossWords) {

        return PuzzleDtoWithoutWords.builder()
                .id(String.valueOf(crossWords.getId()))
                .wins(crossWords.getQuestionInfos().getWinCount())
                .views(crossWords.getQuestionInfos().getViewCount())
                .build();
    }

    @Override
    @Transactional
    public boolean updatePuzzleSuccessCount(String puzzleId) {

        CrossWords crossWords = getCrossWordsById(puzzleId);
        QuestionInfos questionInfos = crossWords.getQuestionInfos();
        questionInfos.addWinCount();
        questionInfoRepository.save(questionInfos);
        return true;
    }


    private Direction getDirection(String sort) {
        String sortName = sort.toUpperCase();
        if (sortName.equals(Direction.ASC.name())) {
            return Direction.ASC;
        }
        if (sortName.equals(Direction.DESC.name())) {
            return Direction.DESC;
        }
        throw new BadRequestException(Error.ILLEGAL_SORT_REQUEST.getMessage());
    }
}
