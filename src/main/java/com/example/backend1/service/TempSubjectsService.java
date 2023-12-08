package com.example.backend1.service;

import com.example.backend1.model.TempSubjectsModel;
import com.example.backend1.repository.TempSubjectsRepository;
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
public class TempSubjectsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TempSubjectsService.class);

    private final String API_URL = "https://wsstag.osu.cz/ws/services/rest2/rozvrhy/getRozvrhoveAkce?zkrPredm={zkratka}&pracoviste={katedra}&rokVarianty=2023&outputFormat=JSON";

    @Autowired
    private TempSubjectsRepository tempSubjectsRepository;

    public void fetchDataAndSave(String katedra, String zkratka) {
        String apiUrlKatedra = API_URL.replace("{katedra}", katedra);
        String apiUrl = apiUrlKatedra.replace("{zkratka}", zkratka);

        try {
            ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(apiUrl, String.class);
            String responseBody = responseEntity.getBody();

            // Log or print the raw response content
            LOGGER.info("Raw API response: {}", responseBody);

            // Deserialize the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Extract the array of programs from the key
            JsonNode programsNode = rootNode.get("rozvrhovaAkce"); // Assuming "rozvrhovaAkce" is the correct key
            TempSubjectsModel[] tempSubjectsArray = objectMapper.treeToValue(programsNode, TempSubjectsModel[].class);

            // Save the fetched data to MongoDB
            if (tempSubjectsArray != null) {
                Arrays.stream(tempSubjectsArray).forEach(this::saveData);
            }
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching data from API: {}", e.getMessage());
            // Handle the exception as needed
        } catch (IOException e) {
            LOGGER.error("Error during JSON deserialization: {}", e.getMessage());
            // Handle the exception as needed
        }
    }

    private void saveData(TempSubjectsModel tempSubject) {
        // Create a new TempSubjectsModel with only the required fields
        TempSubjectsModel savedData = new TempSubjectsModel();
        savedData.setId(tempSubject.getId());
        savedData.setCreatedAt(tempSubject.getCreatedAt());
        savedData.setRoakIdno(tempSubject.getRoakIdno());
        savedData.setNazev(tempSubject.getNazev());
        savedData.setZkratka(tempSubject.getZkratka());
        savedData.setKatedra(tempSubject.getKatedra());
        savedData.setUcitel(tempSubject.getUcitel()); // Assuming TempSubjectsModel has a setUcitel method
        savedData.setBudova(tempSubject.getBudova());
        savedData.setMistnost(tempSubject.getMistnost());
        savedData.setTypAkceZkr(tempSubject.getTypAkceZkr());
        savedData.setDenZkr(tempSubject.getDenZkr());
        savedData.setHodinaSkutOd(tempSubject.getHodinaSkutOd());
        savedData.setHodinaSkutDo(tempSubject.getHodinaSkutDo());
        savedData.setTydenZkr(tempSubject.getTydenZkr());
        // Other fields you want to save can be set similarly

        // Save the modified TempSubjectsModel to the repository
        tempSubjectsRepository.save(savedData);
    }
}
