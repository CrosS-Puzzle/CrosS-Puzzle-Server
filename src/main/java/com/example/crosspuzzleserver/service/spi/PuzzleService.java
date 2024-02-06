package com.example.crosspuzzleserver.service.spi;

import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.service.dto.PuzzleDto;
import com.example.crosspuzzleserver.service.dto.PuzzleListDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PuzzleService {

    PuzzleDto getPuzzleById(String puzzleId, String answer);

    PuzzleListDto getPuzzlesByCategoryName(List<String> categoryNames, int page, int limit, String sort);

    boolean updatePuzzleSuccessCount(String puzzleId);

}
