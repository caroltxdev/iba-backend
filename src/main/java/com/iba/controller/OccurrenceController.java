package com.iba.controller;

import com.iba.domains.enums.OccurrenceType;
import com.iba.dto.request.CreateOccurrenceRequest;
import com.iba.dto.response.OccurrenceResponse;
import com.iba.service.OccurrenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/occurrences")
@RequiredArgsConstructor
@Tag(name = "Occurrences", description = "Endpoints for managing environmental occurrences")

public class OccurrenceController {

    private final OccurrenceService service;

    @PostMapping
    @Operation(summary = "Create a new occurrence", description = "Creates a new environmental occurrence")
    public ResponseEntity<OccurrenceResponse> createOccurrence(
            @Valid @RequestBody CreateOccurrenceRequest request) {
        OccurrenceResponse response = service.createOccurrence(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "List occurrences", description = "Lists all occurrences with optional filters")
    public ResponseEntity<List<OccurrenceResponse>> listOccurrences(
            @Parameter(description = "Filter by occurrence type")
            @RequestParam(required = false) OccurrenceType type,

            @Parameter(description = "Filter by start date (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,

            @Parameter(description = "Filter by end date (inclusive)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        List<OccurrenceResponse> responses = service.listOccurrences(type, start, end);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get occurrence by ID", description = "Retrieves a specific occurrence by its ID")
    public ResponseEntity<OccurrenceResponse> getOccurrenceById(
            @Parameter(description = "Occurrence ID") @PathVariable UUID id) {
        OccurrenceResponse response = service.getOccurrenceById(id);
        return ResponseEntity.ok(response);
    }
}