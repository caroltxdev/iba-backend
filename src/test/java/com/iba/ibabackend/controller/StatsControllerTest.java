package com.iba.ibabackend.controller;

import com.iba.controller.StatsController;
import com.iba.dto.response.SummaryStatsResponse;
import com.iba.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService service;

    @Test
    void getSummaryStats_WithValidParams_ShouldReturn200() throws Exception {
        SummaryStatsResponse stats = SummaryStatsResponse.builder()
                .total(10L)
                .byType(Collections.emptyList())
                .byMonth(Collections.emptyList())
                .build();

        when(service.getSummaryStats(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(stats);

        mockMvc.perform(get("/api/stats/summary")
                        .param("start", "2024-01-01")
                        .param("end", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10));
    }

    @Test
    void getSummaryStats_WithoutStartParam_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/stats/summary")
                        .param("end", "2024-12-31"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void getSummaryStats_WithoutEndParam_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/stats/summary")
                        .param("start", "2024-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }
}
