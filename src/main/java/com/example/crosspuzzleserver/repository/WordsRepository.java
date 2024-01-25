package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.Words;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordsRepository extends MongoRepository<Words, String> {

    Optional<List<Words>> findWordsByCategory(String category);

}
