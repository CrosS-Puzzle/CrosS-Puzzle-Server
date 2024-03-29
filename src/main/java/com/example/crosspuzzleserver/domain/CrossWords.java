package com.example.crosspuzzleserver.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Builder
@Getter
public class CrossWords {

    @Id
    private ObjectId id;

    @DocumentReference
    private List<Category> categories;

    private int rowSize;
    private int colSize;

    @DocumentReference
    private QuestionInfos questionInfos;

    @DocumentReference
    private List<AnswersInfo> answersInfo;

}
