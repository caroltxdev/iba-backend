package com.iba.service;

import com.iba.domain.entity.Occurrence;
import com.iba.domains.enums.OccurrenceType;
import com.iba.dto.request.CreateOccurrenceRequest;
import com.iba.dto.response.OccurrenceResponse;
import com.iba.exception.ResourceNotFoundException;
import com.iba.dto.mapper.OccurrenceMapper;
import com.iba.repository.OccurrenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OccurrenceService {

    private final OccurrenceRepository repository;
    private final OccurrenceMapper mapper;

    @Transactional
    public OccurrenceResponse createOccurrence(CreateOccurrenceRequest request) {
        log.info("Creating new occurrence: type={}, date={}", request.getType(), request.getDate());

        Occurrence occurrence = mapper.toEntity(request);
        Occurrence saved = repository.save(occurrence);

        log.info("Occurrence created successfully: id={}", saved.getId());
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OccurrenceResponse> listOccurrences(OccurrenceType type, LocalDate start, LocalDate end) {
        log.info("Listing occurrences: type={}, start={}, end={}", type, start, end);

        // Converte o enum para String (ou null se type for null)
        String typeStr = type != null ? type.name() : null;

        List<Occurrence> occurrences = repository.findFiltered(typeStr, start, end);

        log.info("Found {} occurrences", occurrences.size());
        return occurrences.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OccurrenceResponse getOccurrenceById(UUID id) {
        log.info("Fetching occurrence by id: {}", id);

        Occurrence occurrence = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Occurrence not found with id: " + id));

        return mapper.toResponse(occurrence);
    }
}