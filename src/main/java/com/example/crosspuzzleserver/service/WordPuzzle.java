package com.example.crosspuzzleserver.service;

import com.example.crosspuzzleserver.domain.AnswersInfo;
import com.example.crosspuzzleserver.domain.Category;
import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.domain.QuestionInfos;
import com.example.crosspuzzleserver.domain.Words;
import com.example.crosspuzzleserver.repository.AnswersInfoRepository;
import com.example.crosspuzzleserver.repository.CategoryRepository;
import com.example.crosspuzzleserver.repository.CrossWordsRepository;
import com.example.crosspuzzleserver.repository.QuestionInfoRepository;
import com.example.crosspuzzleserver.repository.WordsRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WordPuzzle {

    private final CrossWordsRepository crossWordsRepository;
    private final WordsRepository wordsRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionInfoRepository questionInfoRepository;
    private final AnswersInfoRepository answersInfoRepository;

    class WordInfo {
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

    //가로일때 0,1 체크, 세로일때 1,0체크
    static final int[][] insertDirection = {{0, 1}, {1, 0}};

    static List<CheckList> checkLists = new ArrayList<>();

    {
        checkLists.add(new CheckList(new int[]{-1, -1}, List.of(new int[]{-1, 0}, new int[]{0, -1})));
        checkLists.add(new CheckList(new int[]{-1, 1}, List.of(new int[]{-1, 0}, new int[]{0, 1})));
        checkLists.add(new CheckList(new int[]{1, -1}, List.of(new int[]{1, 0}, new int[]{0, -1})));
        checkLists.add(new CheckList(new int[]{1, 1}, List.of(new int[]{1, 0}, new int[]{0, 1})));
    }

    class CheckList {
        int[] check;
        List<int[]> block;

        public CheckList(int[] check, List<int[]> block) {
            this.check = check;
            this.block = block;
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

    public void generateCrossWord(List<ObjectId> categoryIds) {
//        String[] words = {"기차", "차돌박이", "돌잡이", "이쑤시개", "포세이돈", "개선문", "선물포장"};

        //String 이 아니라 Words 오브젝트를 리스트로 넘겨야함.
//        List<Words> wordsList = new ArrayList<>();
//        for (int i = 0; i < words.length; i++) {
//            wordsList.add(
//                    Words.builder()
//                            .value(words[i])
//                            .build()
//            );
//        }

        List<Words> wordsList = new ArrayList<>();
        for (ObjectId categoryId : categoryIds) {
            List<Words> tmpList = wordsRepository.findWordsByCategoryId(categoryId)
                    .orElse(new ArrayList<>());
            wordsList.addAll(tmpList);
        }

        //카테고리 enum을 string으로 변환
        CrossWords createdCrossWords = createPuzzle(wordsList,
                categoryIds.stream().map(categoryRepository::findById).toList());

        System.out.println(" \n");
        System.out.println("rowSize = " + createdCrossWords.getRowSize() + '\n');
        System.out.println("colSize = " + createdCrossWords.getColSize() + '\n');
        System.out.println("cateId = " + createdCrossWords.getCategories().get(0).toString() + '\n');
        for (int i = 0; i < createdCrossWords.getAnswersInfo().size(); i++) {
            System.out.println("answerInfos = " + createdCrossWords.getAnswersInfo().get(i).getWords().getValue());
            System.out.println("x = " + createdCrossWords.getAnswersInfo().get(i).getCoords()[0]);
            System.out.println("y = " + createdCrossWords.getAnswersInfo().get(i).getCoords()[1]);
            System.out.println("direction = " + createdCrossWords.getAnswersInfo().get(i).getDirection() + '\n');
        }

        System.out.println("\n");

        //createPuzzle  에 정답 리스트, 판을 자르기 위한 왼쪽위, 오른쪽 아래 좌표를 반환해야함.
        answersInfoRepository.saveAll(createdCrossWords.getAnswersInfo());
        questionInfoRepository.save(createdCrossWords.getQuestionInfos());

        crossWordsRepository.save(createdCrossWords);
    }


    boolean isValid(char[][] board, int overlapIndex, int overLapPosX, int overLapPosY, Words word,
                    int direction) {

        int boardSize = board.length;
        int wordLength = word.getValue().length();

        int startX = overLapPosX - overlapIndex * insertDirection[direction][0];
        int startY = overLapPosY - overlapIndex * insertDirection[direction][1];

        int endX = startX + (wordLength - 1) * insertDirection[direction][0];
        int endY = startY + (wordLength - 1) * insertDirection[direction][1];

        if (!isWithinRange(boardSize, startX, startY, endX, endY)) {
            return false;
        }

        //바로 이전칸
        int prevX = startX - insertDirection[direction][0];
        int prevY = startY - insertDirection[direction][1];

        //바로 다음칸
        int nextX = endX + insertDirection[direction][0];
        int nextY = endY + insertDirection[direction][1];

        if (prevX >= 0 && prevY >= 0 && nextX < boardSize && nextY < boardSize) {
            if (isCellOccupied(board, prevX, prevY) || isCellOccupied(board, nextX, nextY)) {
                return false;
            }
        }

        for (int i = 0; i < wordLength; i++) {
            int x = startX + i * insertDirection[direction][0];
            int y = startY + i * insertDirection[direction][1];
            if (isCellBlocked(board, x, y)) {
                return false;
            }
            if (!isCellEmpty(board, x, y)) {
                if (board[x][y] != word.getValue().charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isWithinRange(int boardSize, int startX, int startY, int endX, int endY) {
        if (startX < 0 || startY < 0 || endX >= boardSize || endY >= boardSize) {
            return false;
        }
        return true;
    }

    boolean isCellEmpty(char[][] board, int x, int y) {
        return board[x][y] == 'ㅡ';
    }

    boolean isCellBlocked(char[][] board, int x, int y) {
        return board[x][y] == 'x';
    }

    boolean isCellOccupied(char[][] board, int x, int y) {
        return board[x][y] != 'x' && board[x][y] != 'ㅡ';
    }


    List<WordInfo> findOverlaps(Words previousWord, int previousDirection, int startX, int startY,
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

    CrossWords createPuzzle(List<Words> words, List<Category> categories) {
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
                .categories(categories)
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

    PuzzleResult create(char[][] board, List<Words> usedWords, List<Words> unusedWords,
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

            //여기부터 수정
            int x = wordInfo.x;
            int y = wordInfo.y;
            int direction = wordInfo.direction;
            String wordValue = wordInfo.word.getValue();

            int startX = x - wordInfo.indexOfOverlap * insertDirection[direction][0];
            int startY = y - wordInfo.indexOfOverlap * insertDirection[direction][1];

            int endX = startX + (wordValue.length() - 1) * insertDirection[direction][0];
            int endY = startY + (wordValue.length() - 1) * insertDirection[direction][1];

            int prevX = startX - insertDirection[direction][0];
            int prevY = startY - insertDirection[direction][1];

            int nextX = endX + insertDirection[direction][0];
            int nextY = endY + insertDirection[direction][1];

            //블럭처리
            for (int i = 0; i < wordValue.length(); i++) {
                int nx = startX + i * insertDirection[direction][0];
                int ny = startY + i * insertDirection[direction][1];

                board[nx][ny] = wordValue.charAt(i);

                for (CheckList checkList : checkLists) {
                    int[] check = checkList.check;
                    List<int[]> block = checkList.block;

                    if (isCellOccupied(board, nx + check[0], ny + check[1])) {
                        for (int[] b : block) {
                            if (isCellEmpty(board, nx + b[0], ny + b[1]) &&
                                    !isCellBlocked(board, nx + b[0], ny + b[1])) {
                                board[nx + b[0]][ny + b[1]] = 'x';
                            }
                        }
                    }

                }
            }

            //삽입후 단어 앞뒤막기
            if (prevX >= 0 && prevY >= 0 && nextX < board.length && nextY < board.length) {
                if (isCellOccupied(board, prevX, prevY) && isCellOccupied(board, nextX, nextY)) {
                    board[prevX][prevY] = 'x';
                    board[nextX][nextY] = 'x';
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

