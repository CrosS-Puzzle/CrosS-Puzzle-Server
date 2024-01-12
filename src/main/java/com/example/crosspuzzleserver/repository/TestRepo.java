package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.CrossPuzzle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepo extends MongoRepository<CrossPuzzle, String> {

    CrossPuzzle findCrossPuzzleById(String id);
}
