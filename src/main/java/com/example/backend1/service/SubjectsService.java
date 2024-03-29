package com.example.backend1.service;
import com.example.backend1.model.SubjectsModel;
import com.example.backend1.repository.SubjectsRepository;
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
import java.util.Collections;
import java.util.List;

@Service
public class SubjectsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectsService.class);

    private final String API_URL = "https://wsstag.osu.cz/ws/services/rest2/predmety/getPredmetyByObor?oborIdno={oborId}&rok=2023&semestr={semester}&outputFormat=JSON";

    @Autowired
    private SubjectsRepository subjectsRepository;

    public List<SubjectsModel> fetchDataAndSave(String oborId, String semester) {

        String apiUrlKatedra = API_URL.replace("{oborId}", oborId);
        String apiUrl = apiUrlKatedra.replace("{semester}", semester);
        LOGGER.info("FetchandSave");
        try {
            ResponseEntity<String> responseEntity = new RestTemplate().getForEntity(apiUrl, String.class);
            String responseBody = responseEntity.getBody();

            // Log or print the raw response content
            LOGGER.info("Raw API response: {}", responseBody);

            // Deserialize the response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Extract the array of programs from the key
            JsonNode programsNode = rootNode.get("predmetOboru"); // Assuming "rozvrhovaAkce" is the correct key
            SubjectsModel[] tempSubjectsArray = objectMapper.treeToValue(programsNode, SubjectsModel[].class);

            // Save the fetched data to MongoDB
            if (tempSubjectsArray != null) {
                Arrays.stream(tempSubjectsArray).forEach(tempSubject -> saveData(tempSubject, oborId));
                return Arrays.asList(tempSubjectsArray);
            }
            return Collections.emptyList(); // Return an empty list if no data was fetched
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching data from API: {}", e.getMessage());
            // Handle the exception as needed
            return Collections.emptyList();
        } catch (IOException e) {
            LOGGER.error("Error during JSON deserialization: {}", e.getMessage());
            // Handle the exception as needed
            return Collections.emptyList();
        }
    }

    private void saveData(SubjectsModel tempSubject, String oborId) {
        // Create a new SubjectsModel with only the required fields
        List<SubjectsModel> exist = subjectsRepository.findByKatedraAndZkratkaAndOborId(tempSubject.getKatedra(),tempSubject.getZkratka(), oborId);
        if (exist.isEmpty()){
            SubjectsModel savedData = new SubjectsModel();
            savedData.setId(tempSubject.getId());
            savedData.setOborId(oborId);
            savedData.setNazev(tempSubject.getNazev());
            savedData.setZkratka(tempSubject.getZkratka());
            savedData.setKatedra(tempSubject.getKatedra());
            savedData.setStatut(tempSubject.getStatut());
            savedData.setDoporucenyRocnik(tempSubject.getDoporucenyRocnik());
            savedData.setDoporucenySemestr(tempSubject.getDoporucenySemestr());
            subjectsRepository.save(savedData);
        } else{
            LOGGER.info("Skipping save for existing record");
        }
        // Other fields you want to save can be set similarly
        // Save the modified SubjectsModel to the repository

    }
}


