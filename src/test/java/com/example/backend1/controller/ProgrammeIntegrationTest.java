package com.example.backend1.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class ProgrammeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShouldReturnExistingProgrammes() throws Exception {
        // Execute the request and capture the result
        MvcResult result = mockMvc.perform(get("/api/data/getProgrammes")
                        .param("fakultaOboru", "FPR")
                        .param("typ", "Navazující magisterský")
                        .param("forma", "Prezenční")
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
        assertTrue(content.contains("\"nazevCZ\""), "The response content should contain 'nazev' field");


        // You can add additional assertions here if needed
    }
}
