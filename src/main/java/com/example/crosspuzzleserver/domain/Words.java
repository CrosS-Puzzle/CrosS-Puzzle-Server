package com.example.crosspuzzleserver.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Words")
@Getter
public class Words {
    @Id
    String id;

    String category;

    String value;

    String description;

}
