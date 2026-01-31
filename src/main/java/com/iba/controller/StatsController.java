package com.iba.controller;

import com.iba.dto.response.SummaryStatsResponse;
import com.iba.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "Endpoints for occurrence statistics")
public class StatsController {

    private final StatsService service;

    @GetMapping("/summary")
    @Operation(summary = "Get summary statistics",
            description = "Returns total count, count by type, and count by month for a given period")
    public ResponseEntity<SummaryStatsResponse> getSummaryStats(
            @Parameter(description = "Start date (required)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,

            @Parameter(description = "End date (required)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        SummaryStatsResponse response = service.getSummaryStats(start, end);
        return ResponseEntity.ok(response);
    }
}