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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
    public List<Map<String, String>> getProgrammes(
            @RequestParam String fakultaOboru,
            @RequestParam String typ,
            @RequestParam String forma
    ) {
        // Fetch programmes based on faculty, typeOfStudy, and formOfStudy
        List<ProgrammesModel> programmesList = programmesRepository.findByFakultaOboruAndTypAndForma(fakultaOboru, typ, forma);

        // Use a LinkedHashMap to preserve insertion order and to keep track of unique nazevCZ
        Map<String, Map<String, String>> processedProgrammes = new LinkedHashMap<>();

        programmesList.forEach(programme -> {
            // Check if we already have an entry for this nazevCZ
            Map<String, String> existingEntry = processedProgrammes.get(programme.getNazevCZ());
            if (existingEntry == null) {
                // If no entry exists, add the current programme
                Map<String, String> programmeMap = new HashMap<>();
                programmeMap.put("nazevCZ", programme.getNazevCZ());
                programmeMap.put("nazevEN", programme.getNazevEN());
                processedProgrammes.put(programme.getNazevCZ(), programmeMap);
            } else if (programme.getNazevEN() != null && (existingEntry.get("nazevEN") == null || existingEntry.get("nazevEN").isEmpty())) {
                // If an entry exists but the current programme has a non-null (and non-empty) nazevEN,
                // replace the existing entry if the existing nazevEN is null or empty
                existingEntry.put("nazevEN", programme.getNazevEN());
            }
            // If an entry exists and the current programme's nazevEN is null or the existing nazevEN is already non-null,
            // do nothing (preserve the existing entry).
        });
        // Before returning, ensure nazevEN is set to nazevCZ if nazevEN is null
        processedProgrammes.values().forEach(entry -> {
            if (entry.get("nazevEN") == null || entry.get("nazevEN").isEmpty()) {
                entry.put("nazevEN", entry.get("nazevCZ"));
            }
        });
        // Convert the values of the map to a list
        return new ArrayList<>(processedProgrammes.values());
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