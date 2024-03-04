package com.example.crosspuzzleserver.domain;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Builder
public class Category {

    @Id
    private ObjectId id;

    private String name;

    private String koreanName;

}
