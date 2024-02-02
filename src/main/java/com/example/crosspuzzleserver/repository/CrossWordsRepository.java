package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.CrossWords;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CrossWordsRepository extends MongoRepository<CrossWords, String> {

    Optional<CrossWords> findById(String id);

    Page<CrossWords> findAll(Pageable pageable);

}
