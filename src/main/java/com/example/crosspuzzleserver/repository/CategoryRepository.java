package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.Categories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Categories, String> {

    boolean existsById(String categoryId);
}
