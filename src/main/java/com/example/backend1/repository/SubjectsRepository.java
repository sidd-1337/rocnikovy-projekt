package com.example.backend1.repository;
import com.example.backend1.model.SubjectsModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface SubjectsRepository extends MongoRepository<SubjectsModel, String> {
    boolean existsByOborId(String oborId);
    List<SubjectsModel> findByOborId(String oborId);

    List<SubjectsModel> findByOborIdAndDoporucenySemestr(String oborId, String semester);
    List<SubjectsModel> findByOborIdAndDoporucenyRocnik(String oborId, String grade);
    List<SubjectsModel> findByOborIdAndDoporucenyRocnikAndDoporucenySemestr(String highestOborIdStr, String grade, String semester);

    List<SubjectsModel> findByKatedraAndZkratkaAndOborId(String katedra, String zkratka, String oborId);

}
