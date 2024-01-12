package com.example.crosspuzzleserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cross-puzzle")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CrossPuzzle {

    @Id
    private String id;

    private String name;
    private Long age;
}
