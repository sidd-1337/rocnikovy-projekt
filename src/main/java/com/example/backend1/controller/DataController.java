package com.example.backend1.controller;

import com.example.backend1.model.TempSubjectsModel;
import com.example.backend1.repository.TempSubjectsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.backend1.model.ProgrammesModel;
import com.example.backend1.model.SubjectsModel;
import com.example.backend1.repository.ProgrammesRepository;
import com.example.backend1.service.SubjectsService;
import com.example.backend1.repository.SubjectsRepository;
import com.example.backend1.service.TempSubjectsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import java.util.List;
@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private ProgrammesRepository programmesRepository;

    @Autowired
    private SubjectsService subjectsService;

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private TempSubjectsService tempSubjectsService;

    @Autowired
    private TempSubjectsRepository tempSubjectsRepository;

    @GetMapping("/getProgrammes")
    public List<String> getProgrammes(
            @RequestParam String fakultaOboru,
            @RequestParam String typ,
            @RequestParam String forma
    ) {
        // Fetch programmes based on faculty, typeOfStudy, and formOfStudy
        List<ProgrammesModel> programmesList = programmesRepository.findByFakultaOboruAndTypAndForma(fakultaOboru, typ, forma);

        // Extract nazevCZ from the fetched programmes
        List<String> nazevCZList = programmesList.stream()
                .map(ProgrammesModel::getNazevCZ)
                .distinct()
                .collect(Collectors.toList());

        return nazevCZList;
    }
    @GetMapping("/getOborId")
    public List<TempSubjectsModel> getOborId(
            @RequestParam String nazevCZ,
            @RequestParam String fakultaOboru,
            @RequestParam String typ,
            @RequestParam String forma,
            @RequestParam String grade) {

        String programmesList = programmesRepository.findOborIdByNazevCZAndFakultaOboruAndTypAndForma(nazevCZ, fakultaOboru, typ, forma);
        String oborId = "0";
        try {
            JsonNode rootNode = new ObjectMapper().readTree(programmesList);
            oborId = rootNode.path("oborIdno").asText();
        } catch (Exception e) {
            // Handle the exception as needed
            return Collections.emptyList(); // or throw an exception
        }
        // Fetch data for the obtained oborId and save it if it is not already available
        List<SubjectsModel> subjectsList = subjectsRepository.findByOborId(oborId);
        if (subjectsList.isEmpty()) {
            subjectsList = subjectsService.fetchDataAndSave(oborId, "LS");
            // Call fetchDataAndSave for each subject in subjectsList
            subjectsList.forEach(subject -> tempSubjectsService.fetchDataAndSave(subject.getKatedra(), subject.getZkratka()));
        }
        subjectsList = subjectsRepository.findByOborIdAndDoporucenyRocnik(oborId, grade);
        //Fetch data from tempSubjectsRepository based on katedra and zkratka
        List<TempSubjectsModel> tempSubjectsList = new ArrayList<>();
        subjectsList.forEach(subject -> {
            List<TempSubjectsModel> tempSubjectsForSubject = tempSubjectsRepository.findByZkratkaAndKatedra(subject.getZkratka(), subject.getKatedra());
            tempSubjectsList.addAll(tempSubjectsForSubject);
        });

        return tempSubjectsList;
    }
    @GetMapping("/getSubject")
    public List<TempSubjectsModel> getSubject(
            @RequestParam String zkratka,
            @RequestParam String katedra){
    List<TempSubjectsModel> subjectList = tempSubjectsRepository.findByZkratkaAndKatedra(zkratka, katedra);
    if(subjectList.isEmpty()) {
        tempSubjectsService.fetchDataAndSave(katedra,zkratka);
        subjectList = tempSubjectsRepository.findByZkratkaAndKatedra(zkratka,katedra);
        return subjectList;
    }
    return subjectList;
    }

}