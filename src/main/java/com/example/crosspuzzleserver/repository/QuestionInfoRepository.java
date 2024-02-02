package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.QuestionInfos;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionInfoRepository extends MongoRepository<QuestionInfos,String> {
}
