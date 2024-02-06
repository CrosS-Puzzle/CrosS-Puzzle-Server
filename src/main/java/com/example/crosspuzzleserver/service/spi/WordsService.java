package com.example.crosspuzzleserver.service.spi;

import com.example.crosspuzzleserver.service.dto.CheckWordsDto;
import org.springframework.stereotype.Service;

public interface WordsService {

    boolean checkWords(CheckWordsDto checkWordsDto);
}
