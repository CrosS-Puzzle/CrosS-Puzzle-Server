package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.Words;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordsRepository extends MongoRepository<Words, ObjectId> {

    Optional<List<Words>> findWordsByCategory(ObjectId objectId);

    Optional<Words> findById(ObjectId id);
}
