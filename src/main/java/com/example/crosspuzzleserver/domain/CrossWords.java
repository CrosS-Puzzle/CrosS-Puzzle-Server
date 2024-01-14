package com.example.crosspuzzleserver.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Getter
public class CrossWords {

    @Id
    private String id;

    private String cateId;

    private int size;
    
    @DBRef(db = "QuestionInfos")
    private QuestionInfos questionInfos;

    @DBRef(db = "AnswersInfo")
    private List<AnswersInfo> answersInfo;

}
