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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/data")
public class DataController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

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
            @RequestParam String grade,
            @RequestParam String semester) {

        List<String> jsonOborIds = programmesRepository.findOborIdByNazevCZAndFakultaOboruAndTypAndForma(nazevCZ, fakultaOboru, typ, forma);
        LOGGER.info("Obor IDs returned from repository: {}", jsonOborIds);
        int highestOborId = 0;
        ObjectMapper objectMapper = new ObjectMapper();

        for (String json : jsonOborIds) {
            try {
                JsonNode rootNode = objectMapper.readTree(json);
                int oborId = rootNode.path("oborIdno").asInt();
                if (oborId > highestOborId) {
                    highestOborId = oborId;
                }
            } catch (Exception e) {
                LOGGER.error("Error processing JSON: {}", json, e);
            }
        }

        String highestOborIdStr = Integer.toString(highestOborId);

        LOGGER.info("Highest obor ID returned from repository: {}", highestOborIdStr);

        List<SubjectsModel> subjectsList = subjectsRepository.findByOborIdAndDoporucenySemestr(highestOborIdStr, semester);
        if (subjectsList.isEmpty()) {
            subjectsList = subjectsService.fetchDataAndSave(highestOborIdStr, semester);
            subjectsList.forEach(subject -> tempSubjectsService.fetchDataAndSave(subject.getKatedra(), subject.getZkratka()));
        }
        subjectsList = subjectsRepository.findByOborIdAndDoporucenyRocnikAndDoporucenySemestr(highestOborIdStr, grade, semester);

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