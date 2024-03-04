package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.Category;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {

    Category findById(ObjectId id);

}
