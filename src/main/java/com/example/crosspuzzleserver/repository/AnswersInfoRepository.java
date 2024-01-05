package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.AnswersInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswersInfoRepository extends MongoRepository<AnswersInfo,String> {



}
