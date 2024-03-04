package com.example.crosspuzzleserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("QuestionInfos")
@Getter
@Builder
public class QuestionInfos {
    @Id
    private ObjectId id;

    private int viewCount;
    private int winCount;

    public void addViewCount() {
        this.viewCount++;
    }

    public void addWinCount() {
        this.winCount++;
    }

}
