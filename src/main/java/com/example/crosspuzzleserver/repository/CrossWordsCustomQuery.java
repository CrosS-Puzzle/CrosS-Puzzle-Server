package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.CrossWords;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrossWordsCustomQuery {

    private final MongoTemplate mongoTemplate;

    private final String COLLECTION_NAME = "crossWords";
    private final String KEY_FIELD_NAME = "categories";

    public Page<CrossWords> findByCategoryIds(List<String> categoryIds, Pageable pageable) {

        Query query = new Query();
        query.addCriteria(Criteria.where(KEY_FIELD_NAME)
                .in(categoryIds.stream().map(ObjectId::new).collect(Collectors.toList())));
        query.with(pageable);

        List<CrossWords> result = mongoTemplate.find(query, CrossWords.class, COLLECTION_NAME);

        return PageableExecutionUtils.getPage(result, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), CrossWords.class));
    }


}
