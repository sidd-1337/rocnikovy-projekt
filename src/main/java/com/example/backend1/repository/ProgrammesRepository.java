package com.example.backend1.repository;
import com.example.backend1.model.ProgrammesModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProgrammesRepository extends MongoRepository<ProgrammesModel, String> {

}
