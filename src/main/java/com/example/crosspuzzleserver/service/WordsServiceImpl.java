package com.example.crosspuzzleserver.service;

import com.example.crosspuzzleserver.domain.Words;
import com.example.crosspuzzleserver.repository.WordsRepository;
import com.example.crosspuzzleserver.service.dto.CheckWordsDto;
import com.example.crosspuzzleserver.service.spi.WordsService;
import com.example.crosspuzzleserver.util.error.Error;
import com.example.crosspuzzleserver.util.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WordsServiceImpl implements WordsService {

    private final WordsRepository wordsRepository;

    @Override
    public boolean checkWords(CheckWordsDto checkWordsDto) {
        Words words = wordsRepository.findById(new ObjectId(checkWordsDto.getId()))
                .orElseThrow(() ->
                        new NotFoundException(Error.NOT_FOUND_WORDS.getMessage())
                );
        String value = words.getValue();

        return value.equals(checkWordsDto.getInput());
    }


}
