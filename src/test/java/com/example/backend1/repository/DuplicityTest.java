package com.example.backend1.repository;

import com.example.backend1.model.SubjectsModel;
import com.example.backend1.model.TempSubjectsModel;
import com.example.backend1.repository.SubjectsRepository;
import com.example.backend1.repository.TempSubjectsRepository;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
public class DuplicityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubjectsRepository subjectsRepository;
    private String generateCompositeKey(TempSubjectsModel subject) {
        // Concatenate the fields to create a unique composite key for each subject
        // Ensure you handle null values appropriately
        return subject.getZkratka() + "-" +
                subject.getKatedra() + "-" +
                subject.getUcitel() + "-" + // Assuming getUcitel() returns an object with an ID
                subject.getHodinaSkutOd() + "-" +
                subject.getHodinaSkutDo() + "-" +
                subject.getTydenZkr() + "-" +
                subject.getTypAkceZkr() + "-" +
                subject.getMistnost();
    }

    @Autowired
    private TempSubjectsRepository tempSubjectsRepository;
    //Duplicities test
    @Test
    public void testShouldPassWhenNoDuplicitiesInSubjectsRepository() throws Exception {

        mockMvc.perform(get("/api/data/getOborId")
                        .param("nazevCZ", "Sociální práce")
                        .param("fakultaOboru", "FSS")
                        .param("typ", "Bakalářský")
                        .param("forma", "Prezenční")
                        .param("grade", "1")
                        .param("semester", "ZS")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/data/getOborId")
                        .param("nazevCZ", "Sociální práce")
                        .param("fakultaOboru", "FSS")
                        .param("typ", "Bakalářský")
                        .param("forma", "Prezenční")
                        .param("grade", "1")
                        .param("semester", "ZS")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/data/getOborId")
                        .param("nazevCZ", "Sociální práce")
                        .param("fakultaOboru", "FSS")
                        .param("typ", "Bakalářský")
                        .param("forma", "Prezenční")
                        .param("grade", "1")
                        .param("semester", "ZS")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<SubjectsModel> subjects = subjectsRepository.findByKatedraAndZkratkaAndOborId("KAM","BPPLP","3239");
        System.out.println(subjects);
        int listSize = subjects.size();
        assertEquals(listSize,1,"No more than one subject");
    }


    @Test
    public void testShouldPassWhenNoDuplicitiesInTempSubjectsRepository() throws Exception {

        mockMvc.perform(get("/api/data/getOborId")
                        .param("nazevCZ", "Sociální práce")
                        .param("fakultaOboru", "FSS")
                        .param("typ", "Bakalářský")
                        .param("forma", "Prezenční")
                        .param("grade", "1")
                        .param("semester", "ZS")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/data/getOborId")
                        .param("nazevCZ", "Sociální práce")
                        .param("fakultaOboru", "FSS")
                        .param("typ", "Bakalářský")
                        .param("forma", "Prezenční")
                        .param("grade", "1")
                        .param("semester", "ZS")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mockMvc.perform(get("/api/data/getOborId")
                        .param("nazevCZ", "Sociální práce")
                        .param("fakultaOboru", "FSS")
                        .param("typ", "Bakalářský")
                        .param("forma", "Prezenční")
                        .param("grade", "1")
                        .param("semester", "ZS")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<TempSubjectsModel> subjects = tempSubjectsRepository.findByZkratkaAndKatedra("BPPLP", "KAM");
        System.out.println(subjects);

        // Generate composite keys and count their occurrences
        Map<String, Long> keyOccurrences = subjects.stream()
                .map(subject -> generateCompositeKey(subject))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Find any composite keys that occur more than once
        long duplicateCount = keyOccurrences.values().stream().filter(count -> count > 1).count();

        assertEquals(duplicateCount, 0,"There should be no duplicates based on the composite key");
    }

}
