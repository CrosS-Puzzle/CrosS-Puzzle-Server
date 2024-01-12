package com.example.crosspuzzleserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("QuestionInfos")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionInfos {
    @Id
    private String id;

    private int viewCount;
    private int winCount;
}
