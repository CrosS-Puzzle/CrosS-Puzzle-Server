package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.CrossWords;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CrossWordsRepository extends MongoRepository<CrossWords, String> {



}
