package com.example.backend1.service;
import com.example.backend1.model.ProgrammesModel;
import com.example.backend1.repository.ProgrammesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;

@Service
public class ProgrammesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProgrammesService.class);

    private final String API_URL = "https://wsstag.osu.cz/ws/services/rest2/programy/getOboryQRAMInfo?outputFormat=JSON&fakulta={faculty}&rok=2023/2024&pouzePlatne=true";

    @Autowired
    private ProgrammesRepository programmesRepository;

    public void fetchDataAndSave(String faculty) {
        String apiUrl = API_URL.replace("{faculty}", faculty);

        try {
            ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(apiUrl, String.class);
            String responseBody = responseEntity.getBody();

            // Log or print the raw response content
            LOGGER.info("Raw API response: {}", responseBody);

            // Deserialize the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Extract the array of programs from the key
            JsonNode programsNode = rootNode.get("oborQRAMInfo");
            ProgrammesModel[] programmes = objectMapper.treeToValue(programsNode, ProgrammesModel[].class);

            // Save the fetched data to MongoDB
            if (programmes != null) {
                Arrays.stream(programmes).forEach(programmesRepository::save);
            }
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching data from API: {}", e.getMessage());
            // Handle the exception as needed
        } catch (IOException e) {
            LOGGER.error("Error during JSON deserialization: {}", e.getMessage());
            // Handle the exception as needed
        }
    }
}
