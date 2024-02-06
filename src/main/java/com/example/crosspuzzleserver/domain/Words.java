package com.example.crosspuzzleserver.domain;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("words")
@Getter
@Builder
public class Words {
    @Id
    ObjectId id;

    String category;

    String value;

    String description;

}
