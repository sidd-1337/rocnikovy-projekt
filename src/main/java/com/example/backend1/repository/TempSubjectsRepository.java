package com.example.backend1.repository;

import com.example.backend1.model.TempSubjectsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempSubjectsRepository extends MongoRepository<TempSubjectsModel, String> {
    // Add custom queries or methods if needed
}
