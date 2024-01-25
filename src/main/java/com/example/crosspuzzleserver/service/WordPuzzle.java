package com.example.crosspuzzleserver.service;

import com.example.crosspuzzleserver.domain.AnswersInfo;
import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.domain.QuestionInfos;
import com.example.crosspuzzleserver.domain.Words;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WordPuzzle {

//    private static CrossWordsRepository crossWordsRepository;

    public static void generateCrossWord() {
        String[] words = {"기차", "차돌박이", "돌잡이", "이쑤시개", "포세이돈", "개선문", "선물포장"};

        //String 이 아니라 Words 오브젝트를 리스트로 넘겨야함.
        List<Words> wordsList = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            wordsList.add(
                    Words.builder()
                            .value(words[i])
                            .build()
            );
        }

        String categoryId = "cateId";
        CrossWords createdCrossWords = createPuzzle(wordsList, categoryId);

        System.out.println(" \n");
        System.out.println("rowSize = " + createdCrossWords.getRowSize() + '\n');
        System.out.println("colSize = " + createdCrossWords.getColSize() + '\n');
        System.out.println("cateId = " + createdCrossWords.getCateId() + '\n');
        for (int i = 0; i < createdCrossWords.getAnswersInfo().size(); i++) {
            System.out.println("answerInfos = " + createdCrossWords.getAnswersInfo().get(i).getWords().getValue());
            System.out.println("x = " + createdCrossWords.getAnswersInfo().get(i).getCoords()[0]);
            System.out.println("y = " + createdCrossWords.getAnswersInfo().get(i).getCoords()[1]);
            System.out.println("direction = " + createdCrossWords.getAnswersInfo().get(i).getDirection() + '\n');
        }

        System.out.println("\n");

        //createPuzzle  에 정답 리스트, 판을 자르기 위한 왼쪽위, 오른쪽 아래 좌표를 반환해야함.
//        crossWordsRepository.save(createdCrossWords);
    }

    static class WordInfo {
        int x, y, direction, indexOfOverlap;
        Words word;

        public WordInfo(int x, int y, int direction, int indexOfOverlap, Words word) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.indexOfOverlap = indexOfOverlap;
            this.word = word;
        }
    }

    @Builder
    static class PuzzleResult {
        List<AnswersInfoDAO> answersInfoList;
        int[] minIndex;
        int[] maxIndex;
    }

    @Builder
    @Getter
    static class AnswersInfoDAO {
        int[] coords;
        int direction;
        Words words;
    }

    static boolean isValid(char[][] board, int overlapIndex, int x, int y, Words word, int direction) {
        int boardLength = board.length;

        if (direction == 0) { // Horizontal
            if (y - overlapIndex < 0 || y + word.getValue().length() - overlapIndex > boardLength) {
                return false;
            }

            if (board[x][y - overlapIndex - 1] != 'ㅡ' && board[x][y - overlapIndex - 1] != 'x') {
                return false;
            }

            if (board[x][y + word.getValue().length() - overlapIndex] != 'ㅡ'
                    && board[x][y + word.getValue().length() - overlapIndex] != 'x') {
                return false;
            }
        } else { // Vertical
            if (x - overlapIndex < 0 || x + word.getValue().length() - overlapIndex > boardLength) {
                return false;
            }

            if (board[x - overlapIndex - 1][y] != 'ㅡ' && board[x - overlapIndex - 1][y] != 'x') {
                return false;
            }

            if (board[x + word.getValue().length() - overlapIndex][y] != 'ㅡ'
                    && board[x + word.getValue().length() - overlapIndex][y] != 'x') {
                return false;
            }
        }

        for (int i = -overlapIndex; i < word.getValue().length() - overlapIndex; i++) {
            if (direction == 0) { // Horizontal
                if (board[x][y + i] == 'x') {
                    return false;
                }

                if (board[x][y + i] != 'ㅡ' && board[x][y + i] != word.getValue().charAt(i + overlapIndex)) {
                    return false;
                }
            } else { // Vertical
                if (board[x + i][y] == 'x') {
                    return false;
                }

                if (board[x + i][y] != 'ㅡ' && board[x + i][y] != word.getValue().charAt(i + overlapIndex)) {
                    return false;
                }
            }
        }

        return true;
    }

    static List<WordInfo> findOverlaps(Words previousWord, int previousDirection, int startX, int startY,
                                       List<Words> unusedWords) {
        List<WordInfo> result = new ArrayList<>();

        for (int i = 0; i < previousWord.getValue().length(); i++) {
            List<Words> candidates = new ArrayList<>();
            for (Words word : unusedWords) {
                if (word.getValue().contains(String.valueOf(previousWord.getValue().charAt(i)))) {
                    candidates.add(word);
                }
            }

            for (Words candidate : candidates) {
                int x = (previousDirection == 1) ? startX + i : startX;
                int y = (previousDirection == 0) ? startY + i : startY;

                int indexOfOverlap = candidate.getValue().indexOf(previousWord.getValue().charAt(i));

                WordInfo info = new WordInfo(x, y, (previousDirection == 0) ? 1 : 0, indexOfOverlap, candidate);
                result.add(info);
            }
        }

        return result;
    }

    static CrossWords createPuzzle(List<Words> words, String categroyId) {
        char[][] board = new char[25][25];
        for (int i = 0; i < 25; i++) {
            for (int j = 0; j < 25; j++) {
                board[i][j] = 'ㅡ';
            }
        }
        List<Words> usedWords = new ArrayList<>();

        int x = 10;
        int y = 10;

        for (int i = 0; i < words.get(0).getValue().length(); i++) {
            board[x][y + i] = words.get(0).getValue().charAt(i);
            usedWords.add(words.get(0));
        }

        System.out.println(words.get(0).getValue() + " " + x + " " + y + " " + 0);

        board[x][y - 1] = 'x';
        board[x][y + words.get(0).getValue().length()] = 'x';

        List<WordInfo> overlaps = findOverlaps(words.get(0), 0, x, y, words.subList(1, words.size()));

        PuzzleResult puzzleResult = create(board, usedWords, words.subList(1, words.size()), overlaps);

        //create 에서 보드를 자를 왼쪽위 좌표와 오른쪽 아래 좌표를 가져와야함.
        int rowSize = puzzleResult.maxIndex[0] - puzzleResult.minIndex[0];
        int colSize = puzzleResult.maxIndex[1] - puzzleResult.minIndex[1];

        List<AnswersInfo> answersInfoList = new ArrayList<>();
        answersInfoList.add(AnswersInfo.builder()
                .coords(new int[]{x - puzzleResult.minIndex[0], y - puzzleResult.minIndex[1]})
                .direction(0)
                .words(words.get(0))
                .build());
        answersInfoList.addAll(
                puzzleResult.answersInfoList.stream().map((answersInfo ->
                        AnswersInfo.builder()
                                .words(answersInfo.words)
                                .direction(answersInfo.direction)
                                .coords(
                                        new int[]{answersInfo.coords[0] - puzzleResult.minIndex[0],
                                                answersInfo.coords[1] - puzzleResult.minIndex[1]}
                                )
                                .build()
                )).toList()
        );

        //그리고 여기서 자르고 answerInfoList를 업데이트하고 반환. or CrossWords 생성.
        QuestionInfos questionInfos = QuestionInfos.builder()
                .viewCount(0)
                .winCount(0)
                .build();
        CrossWords crossWords = CrossWords.builder()
                .cateId(categroyId)
                .rowSize(rowSize)
                .colSize(colSize)
                .answersInfo(answersInfoList)
                .questionInfos(questionInfos)
                .build();

        for (char[] row : board) {
            for (char cell : row) {
                System.out.print(cell + "  ");
            }
            System.out.println();
        }

        return crossWords;
    }

    static PuzzleResult create(char[][] board, List<Words> usedWords, List<Words> unusedWords,
                               List<WordInfo> connectionInfo) {
        List<WordInfo> newConnectionInfo = new ArrayList<>(connectionInfo);
        char[][] newBoard = board;
        List<AnswersInfoDAO> answersInfoList = new ArrayList<>();
        int[] minIndex = new int[]{10, 10};
        int[] maxIndex = new int[]{10, usedWords.get(0).getValue().length() + 10};

        while (!newConnectionInfo.isEmpty()) {
            WordInfo wordInfo = newConnectionInfo.remove(0);

            if (!isValid(newBoard, wordInfo.indexOfOverlap, wordInfo.x, wordInfo.y, wordInfo.word, wordInfo.direction)
                    ||
                    usedWords.contains(wordInfo.word)) {
                continue;
            }

            usedWords.add(wordInfo.word);
            AnswersInfoDAO additionalAnswersInfo = AnswersInfoDAO.builder()
                    .words(wordInfo.word)
                    .coords(new int[]{
                            (wordInfo.direction == 0) ? wordInfo.x : wordInfo.x - wordInfo.indexOfOverlap,
                            (wordInfo.direction == 0) ? wordInfo.y - wordInfo.indexOfOverlap : wordInfo.y
                    })
                    .direction(wordInfo.direction)
                    .build();
            answersInfoList.add(additionalAnswersInfo);

            if (minIndex[0] > additionalAnswersInfo.getCoords()[0]) {
                minIndex[0] = additionalAnswersInfo.getCoords()[0];
            }
            if (minIndex[1] > additionalAnswersInfo.getCoords()[1]) {
                minIndex[1] = additionalAnswersInfo.getCoords()[1];
            }

            //가로일때 board의 max 값 설정하기
            if (wordInfo.direction == 0) {
                if (maxIndex[1] < additionalAnswersInfo.getCoords()[1] + wordInfo.word.getValue().length()) {
                    maxIndex[1] = additionalAnswersInfo.getCoords()[1] + wordInfo.word.getValue().length();
                }
            } else { //세로일때
                if (maxIndex[0] < additionalAnswersInfo.getCoords()[0] + wordInfo.word.getValue().length()) {
                    maxIndex[0] = additionalAnswersInfo.getCoords()[0] + wordInfo.word.getValue().length();
                }
            }

            System.out.println(wordInfo.word + " " +
                    ((wordInfo.direction == 0) ? wordInfo.x : wordInfo.x - wordInfo.indexOfOverlap) + " " +
                    ((wordInfo.direction == 0) ? wordInfo.y - wordInfo.indexOfOverlap : wordInfo.y) + " " +
                    wordInfo.direction);

            for (int i = -wordInfo.indexOfOverlap; i < wordInfo.word.getValue().length() - wordInfo.indexOfOverlap;
                 i++) {
                if (wordInfo.direction == 0) { // Horizontal
                    newBoard[wordInfo.x][wordInfo.y + i] = wordInfo.word.getValue().charAt(i + wordInfo.indexOfOverlap);

                    if (newBoard[wordInfo.x - 1][wordInfo.y + i - 1] != 'ㅡ' &&
                            newBoard[wordInfo.x - 1][wordInfo.y + i - 1] != 'x' &&
                            newBoard[wordInfo.x - 1][wordInfo.y + i] == 'ㅡ') {
                        newBoard[wordInfo.x - 1][wordInfo.y + i] = 'x';
                    }

                    if (newBoard[wordInfo.x + 1][wordInfo.y + i - 1] != 'ㅡ' &&
                            newBoard[wordInfo.x + 1][wordInfo.y + i - 1] != 'x' &&
                            newBoard[wordInfo.x + 1][wordInfo.y + i] == 'ㅡ') {
                        newBoard[wordInfo.x + 1][wordInfo.y + i] = 'x';
                    }

                    if (newBoard[wordInfo.x - 1][wordInfo.y + i + 1] != 'ㅡ' &&
                            newBoard[wordInfo.x - 1][wordInfo.y + i + 1] != 'x' &&
                            newBoard[wordInfo.x - 1][wordInfo.y + i] == 'ㅡ') {
                        newBoard[wordInfo.x - 1][wordInfo.y + i] = 'x';
                    }

                    if (newBoard[wordInfo.x + 1][wordInfo.y + i + 1] != 'ㅡ' &&
                            newBoard[wordInfo.x + 1][wordInfo.y + i + 1] != 'x' &&
                            newBoard[wordInfo.x + 1][wordInfo.y + i] == 'ㅡ') {
                        newBoard[wordInfo.x + 1][wordInfo.y + i] = 'x';
                    }
                } else { // Vertical
                    newBoard[wordInfo.x + i][wordInfo.y] = wordInfo.word.getValue().charAt(i + wordInfo.indexOfOverlap);

                    if (newBoard[wordInfo.x + i - 1][wordInfo.y - 1] != 'ㅡ' &&
                            newBoard[wordInfo.x + i - 1][wordInfo.y - 1] != 'x' &&
                            newBoard[wordInfo.x + i][wordInfo.y - 1] == 'ㅡ') {
                        newBoard[wordInfo.x + i][wordInfo.y - 1] = 'x';
                    }

                    if (newBoard[wordInfo.x + i - 1][wordInfo.y + 1] != 'ㅡ' &&
                            newBoard[wordInfo.x + i - 1][wordInfo.y + 1] != 'x' &&
                            newBoard[wordInfo.x + i][wordInfo.y + 1] == 'ㅡ') {
                        newBoard[wordInfo.x + i][wordInfo.y + 1] = 'x';
                    }

                    if (newBoard[wordInfo.x + i + 1][wordInfo.y - 1] != 'ㅡ' &&
                            newBoard[wordInfo.x + i + 1][wordInfo.y - 1] != 'x' &&
                            newBoard[wordInfo.x + i][wordInfo.y - 1] == 'ㅡ') {
                        newBoard[wordInfo.x + i][wordInfo.y - 1] = 'x';
                    }

                    if (newBoard[wordInfo.x + i + 1][wordInfo.y + 1] != 'ㅡ' &&
                            newBoard[wordInfo.x + i + 1][wordInfo.y + 1] != 'x' &&
                            newBoard[wordInfo.x + i][wordInfo.y + 1] == 'ㅡ') {
                        newBoard[wordInfo.x + i][wordInfo.y + 1] = 'x';
                    }
                }
            }

            if (wordInfo.direction == 0) { // Horizontal
                if (newBoard[wordInfo.x][wordInfo.y - wordInfo.indexOfOverlap - 1] == 'ㅡ') {
                    newBoard[wordInfo.x][wordInfo.y - wordInfo.indexOfOverlap - 1] = 'x';
                }

                if (newBoard[wordInfo.x][wordInfo.y + wordInfo.word.getValue().length() - wordInfo.indexOfOverlap]
                        == 'ㅡ') {
                    newBoard[wordInfo.x][wordInfo.y + wordInfo.word.getValue().length()
                            - wordInfo.indexOfOverlap] = 'x';
                }
            } else { // Vertical
                if (newBoard[wordInfo.x - wordInfo.indexOfOverlap - 1][wordInfo.y] == 'ㅡ') {
                    newBoard[wordInfo.x - wordInfo.indexOfOverlap - 1][wordInfo.y] = 'x';
                }

                if (newBoard[wordInfo.x + wordInfo.word.getValue().length() - wordInfo.indexOfOverlap][wordInfo.y]
                        == 'ㅡ') {
                    newBoard[wordInfo.x + wordInfo.word.getValue().length()
                            - wordInfo.indexOfOverlap][wordInfo.y] = 'x';
                }
            }

            List<WordInfo> additionalConnectionInfo = findOverlaps(
                    wordInfo.word,
                    wordInfo.direction,
                    (wordInfo.direction == 0) ? wordInfo.x : wordInfo.x - wordInfo.indexOfOverlap,
                    (wordInfo.direction == 0) ? wordInfo.y - wordInfo.indexOfOverlap : wordInfo.y,
                    unusedWords
            );

            newConnectionInfo.addAll(additionalConnectionInfo.stream()
                    .filter(info -> !usedWords.contains(info.word))
                    .toList()
            );

        }

        PuzzleResult puzzleResult = PuzzleResult.builder()
                .answersInfoList(answersInfoList)
                .minIndex(minIndex)
                .maxIndex(maxIndex)
                .build();

        return puzzleResult;

    }
}

