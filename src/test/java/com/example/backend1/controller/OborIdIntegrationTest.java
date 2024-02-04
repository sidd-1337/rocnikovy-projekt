package com.example.backend1.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestParam;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class OborIdIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShouldReturnExistingTimetable() throws Exception {
        // Execute the request and capture the result
        MvcResult result = mockMvc.perform(get("/api/data/getOborId")
                        .param("nazevCZ", "Umělá inteligence")
                        .param("fakultaOboru", "FPR")
                        .param("typ", "Navazující magisterský")
                        .param("forma", "Prezenční")
                        .param("grade", "1")
                        .param("semester", "ZS")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn(); // Store the result for further examination

        // Extract the response content as a String
        String content = result.getResponse().getContentAsString();
        // Print the response content
        //System.out.println(content);
        assertFalse(content.isEmpty(), "The response content should not be empty");
        assertTrue(content.contains("\"nazev\""), "The response content should contain 'nazev' field");


        // You can add additional assertions here if needed
    }
}
