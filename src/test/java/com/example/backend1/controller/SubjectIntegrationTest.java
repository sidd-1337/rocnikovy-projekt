package com.example.backend1.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SubjectIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShouldReturnExistingSubject() throws Exception {
        // Execute the request and capture the result
        MvcResult result = mockMvc.perform(get("/api/data/getSubject")
                        .param("zkratka", "7DEEP")
                        .param("katedra", "KIP")
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
        //assertTrue(content.); //check body
        // You can add additional assertions here if needed
    }

    @Test
    public void testShouldFailNonExistingSubject() throws Exception {
        // Execute the request and capture the result
        MvcResult result = mockMvc.perform(get("/api/data/getSubject")
                        .param("zkratka", "7DEEPi")
                        .param("katedra", "KIP")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn(); // Store the result for further examination

        // Extract the response content as a String
        String content = result.getResponse().getContentAsString();
        // Print the response content
        //System.out.println("test me");
        //System.out.println(content);
        assertFalse(content.isEmpty(), "The response content should not be empty");
        assertFalse(content.contains("\"nazev\""), "The response content should contain 'nazev' field");

        // You can add additional assertions here if needed
    }
}
