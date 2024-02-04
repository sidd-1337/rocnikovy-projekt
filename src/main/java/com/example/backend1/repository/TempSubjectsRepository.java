package com.example.backend1.repository;

import com.example.backend1.model.TempSubjectsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempSubjectsRepository extends MongoRepository<TempSubjectsModel, String> {
    List<TempSubjectsModel> findByZkratkaAndKatedra(String zkratka, String katedra);
    List<TempSubjectsModel> findByZkratkaAndKatedraAndUcitelAndHodinaSkutOdAndHodinaSkutDoAndTydenZkrAndTypAkceZkrAndMistnost(String zkratka, String katedra, Object ucitel, Object hodinaSkutOd, Object hodinaSkutDo, String denZkr,String typAkceZkr, String mistnost);

}
