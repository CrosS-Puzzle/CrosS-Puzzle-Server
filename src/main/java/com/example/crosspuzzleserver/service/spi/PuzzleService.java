package com.example.crosspuzzleserver.service.spi;

import com.example.crosspuzzleserver.domain.CrossWords;
import com.example.crosspuzzleserver.service.dto.PuzzleDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface PuzzleService {

    PuzzleDto getPuzzleById(String puzzleId, String answer);

    Page<CrossWords> getPuzzlesByCategoryName(List<String> categoryNames, int page, int limit, String sort);


}
