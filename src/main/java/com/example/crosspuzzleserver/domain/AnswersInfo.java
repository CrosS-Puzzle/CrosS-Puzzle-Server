package com.example.crosspuzzleserver.domain;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("AnswersInfo")
@Getter
@Builder
public class AnswersInfo {

    @Id
    private ObjectId id;

    int[] coords;

    int direction;  //0이면 가로방향, 1이면 세로방향

    private Words words;

}
