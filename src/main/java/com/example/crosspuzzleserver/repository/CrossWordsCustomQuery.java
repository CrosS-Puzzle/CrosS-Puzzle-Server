package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.CrossWords;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

@Component
public class CrossWordsCustomQuery {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<CrossWords> findByCategories(List<String> categoryNames, Pageable pageable) {

        Query query = new Query();
        query.addCriteria(Criteria.where("categories").in(categoryNames));
        query.with(pageable);

        System.out.println(query.toString());
        List<CrossWords> result = mongoTemplate.find(query, CrossWords.class, "crossWords");
        System.out.println(result.get(0).getCategories());

        return PageableExecutionUtils.getPage(result, pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), CrossWords.class));
    }


}
