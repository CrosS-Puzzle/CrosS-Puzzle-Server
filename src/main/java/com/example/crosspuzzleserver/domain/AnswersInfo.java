package com.example.crosspuzzleserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document("AnswersInfo")
@Getter
@Builder
public class AnswersInfo {

    @Id
    private String id;

    int[] coords;

    int direction;  //0이면 가로방향, 1이면 세로방향

    private Words words;

}
