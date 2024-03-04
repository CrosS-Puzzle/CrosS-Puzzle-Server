package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.AnswersInfo;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswersInfoRepository extends MongoRepository<AnswersInfo,String> {

    List<AnswersInfo> findAllBy();

}
