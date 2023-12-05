package com.example.backend1.repository;
import com.example.backend1.model.SubjectsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SubjectsRepository extends MongoRepository<SubjectsModel, String> {

}
