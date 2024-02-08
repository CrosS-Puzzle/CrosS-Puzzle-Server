package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.Words;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordsRepository extends MongoRepository<Words, String> {

    Optional<List<Words>> findWordsByCategory(String category);

    Optional<List<Words>> findWordsByCategoryId(ObjectId id);

    Optional<Words> findById(ObjectId id);
}
