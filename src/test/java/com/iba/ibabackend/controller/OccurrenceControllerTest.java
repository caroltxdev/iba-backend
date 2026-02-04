package com.iba.ibabackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iba.controller.OccurrenceController;
import com.iba.domains.enums.OccurrenceType;
import com.iba.dto.request.CreateOccurrenceRequest;
import com.iba.dto.response.OccurrenceResponse;
import com.iba.exception.ResourceNotFoundException;
import com.iba.service.OccurrenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OccurrenceController.class)
class OccurrenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OccurrenceService service;

    private CreateOccurrenceRequest validRequest;
    private OccurrenceResponse response;

    @BeforeEach
    void setUp() {
        validRequest = CreateOccurrenceRequest.builder()
                .type(OccurrenceType.QUEIMADA)
                .date(LocalDate.of(2024, 1, 15))
                .description("Test occurrence description")
                .latitude(new BigDecimal("-15.123456"))
                .longitude(new BigDecimal("-47.654321"))
                .build();

        response = OccurrenceResponse.builder()
                .id(UUID.randomUUID())
                .type(OccurrenceType.QUEIMADA)
                .date(LocalDate.of(2024, 1, 15))
                .description("Test occurrence description")
                .latitude(new BigDecimal("-15.123456"))
                .longitude(new BigDecimal("-47.654321"))
                .createdAt(Instant.now())
                .build();
    }

    @Test
    void createOccurrence_WithValidData_ShouldReturn201() throws Exception {
        when(service.createOccurrence(any(CreateOccurrenceRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/occurrences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("QUEIMADA"))
                .andExpect(jsonPath("$.description").value("Test occurrence description"));
    }

    @Test
    void createOccurrence_WithInvalidData_ShouldReturn400() throws Exception {
        CreateOccurrenceRequest invalidRequest = CreateOccurrenceRequest.builder()
                .type(OccurrenceType.QUEIMADA)
                .date(LocalDate.of(2024, 1, 15))
                .description("Bad")
                .latitude(new BigDecimal("100"))
                .longitude(new BigDecimal("-47.654321"))
                .build();

        mockMvc.perform(post("/api/occurrences")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void listOccurrences_ShouldReturn200() throws Exception {
        when(service.listOccurrences(any(), any(), any())).thenReturn(Arrays.asList(response));

        mockMvc.perform(get("/api/occurrences"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void getOccurrenceById_WhenExists_ShouldReturn200() throws Exception {
        UUID id = response.getId();
        when(service.getOccurrenceById(id)).thenReturn(response);

        mockMvc.perform(get("/api/occurrences/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getOccurrenceById_WhenNotExists_ShouldReturn404() throws Exception {
        UUID id = UUID.randomUUID();
        when(service.getOccurrenceById(id))
                .thenThrow(new ResourceNotFoundException("Occurrence not found"));

        mockMvc.perform(get("/api/occurrences/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Occurrence not found"));
    }
}
