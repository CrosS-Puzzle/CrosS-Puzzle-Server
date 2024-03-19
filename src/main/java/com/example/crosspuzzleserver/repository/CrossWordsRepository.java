package com.example.crosspuzzleserver.repository;

import com.example.crosspuzzleserver.domain.Category;
import com.example.crosspuzzleserver.domain.CrossWords;
import java.util.List;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CrossWordsRepository extends MongoRepository<CrossWords, String> {

    Optional<CrossWords> findById(ObjectId id);

    Page<CrossWords> findAll(Pageable pageable);

    long countByCategories(Category category);

    @Query("SELECT cw FROM CrossWords cw WHERE cw.categoryId IN :categoryIds")
    Optional<List<CrossWords>> getCrossWordsByCategoryIds(@Param("categoryIds") List<ObjectId> categoryIds);

}
