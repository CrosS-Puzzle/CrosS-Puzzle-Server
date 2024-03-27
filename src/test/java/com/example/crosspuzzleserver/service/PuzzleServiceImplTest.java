package com.example.crosspuzzleserver.service;

import com.example.crosspuzzleserver.repository.CrossWordsCustomQuery;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import com.example.crosspuzzleserver.service.puzzle.spi.PuzzleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PuzzleServiceImplTest {

    PuzzleService puzzleService;

    @MockBean
    CrossWordsRepository crossWordsRepository;

    CrossWordsCustomQuery crossWordsCustomQuery;

//    @BeforeEach
//    void setUp() {
////        puzzleService = new PuzzleServiceImpl(crossWordsRepository, categoryRepository);
//        crossWordsCustomQuery = new CrossWordsCustomQuery();
//    }

//    @Test
//    @DisplayName("아이디로 퍼즐 조회 테스트")
//    void getPuzzleById() {
//
//        //given
//        List<AnswersInfo> answersInfos = new ArrayList<>();
//        Words words = Words.builder()
//                .id("423")
//                .value("word value")
//                .build();
//        AnswersInfo answersInfo = AnswersInfo.builder()
//                .id("44")
//                .coords(new int[]{1,2})
//                .words(words)
//                .build();
//        answersInfos.add(answersInfo);
//
//        CrossWords crossWords = CrossWords.builder()
//                .id("1234")
//                .questionInfos(QuestionInfos.builder()
//                        .winCount(0)
//                        .viewCount(0)
//                        .build())
//                .size(10)
//                .cateId("1")
//                .answersInfo(answersInfos)
//                .build();
//
//        Mockito.when(crossWordsRepository.findById("1234")).thenReturn(Optional.of(crossWords));
//        //when
//        //then
//        PuzzleDto puzzleDto = puzzleService.getPuzzleById("1234", "true");
//
//        assertAll(
//                () -> assertEquals(crossWords.getId(), puzzleDto.getId()),
//                () -> assertEquals(crossWords.getSize(), puzzleDto.getSize())
//        );
//
//    }

    @Test
    void getPuzzlesByCategoryList() {

    }

}