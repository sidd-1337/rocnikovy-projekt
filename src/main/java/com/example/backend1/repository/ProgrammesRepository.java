package com.example.backend1.repository;

import com.example.backend1.model.ProgrammesModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProgrammesRepository extends MongoRepository<ProgrammesModel, String> {
    List<ProgrammesModel> findByFakultaOboruAndTypAndForma(String fakultaOboru, String typ, String forma);
    List<String> findOborIdByNazevCZAndFakultaOboruAndTypAndForma(String nazevCZ, String fakultaOboru, String typ, String forma);
}
