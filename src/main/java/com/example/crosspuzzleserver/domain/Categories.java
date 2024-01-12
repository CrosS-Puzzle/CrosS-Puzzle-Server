package com.example.crosspuzzleserver.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("categories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Categories {

    @Id
    private String id;

    private List<String> selected;

}
