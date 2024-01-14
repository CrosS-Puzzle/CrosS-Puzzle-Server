package com.example.crosspuzzleserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("AnswersInfo")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswersInfo {

    @Id
    private String id;

    int[] coords;

    String direction;

    @DBRef(db = "Words")
    Words words;

}
