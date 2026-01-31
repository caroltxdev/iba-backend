package com.iba.ibabackend.service;

import com.iba.domain.entity.Occurrence;
import com.iba.domains.enums.OccurrenceType;
import com.iba.dto.request.CreateOccurrenceRequest;
import com.iba.dto.response.OccurrenceResponse;
import com.iba.exception.ResourceNotFoundException;
import com.iba.dto.mapper.OccurrenceMapper;
import com.iba.repository.OccurrenceRepository;
import com.iba.service.OccurrenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OccurrenceServiceTest {

    @Mock
    private OccurrenceRepository repository;

    @Mock
    private OccurrenceMapper mapper;

    @InjectMocks
    private OccurrenceService service;

    private CreateOccurrenceRequest request;
    private Occurrence occurrence;
    private OccurrenceResponse response;

    @BeforeEach
    void setUp() {
        request = CreateOccurrenceRequest.builder()
                .type(OccurrenceType.QUEIMADA)
                .date(LocalDate.of(2024, 1, 15))
                .description("Test occurrence")
                .latitude(new BigDecimal("-15.123456"))
                .longitude(new BigDecimal("-47.654321"))
                .build();

        occurrence = Occurrence.builder()
                .id(UUID.randomUUID())
                .type(OccurrenceType.QUEIMADA)
                .date(LocalDate.of(2024, 1, 15))
                .description("Test occurrence")
                .latitude(new BigDecimal("-15.123456"))
                .longitude(new BigDecimal("-47.654321"))
                .createdAt(Instant.now())
                .build();

        response = OccurrenceResponse.builder()
                .id(occurrence.getId())
                .type(occurrence.getType())
                .date(occurrence.getDate())
                .description(occurrence.getDescription())
                .latitude(occurrence.getLatitude())
                .longitude(occurrence.getLongitude())
                .createdAt(occurrence.getCreatedAt())
                .build();
    }

    @Test
    void createOccurrence_ShouldReturnCreatedOccurrence() {
        when(mapper.toEntity(request)).thenReturn(occurrence);
        when(repository.save(any(Occurrence.class))).thenReturn(occurrence);
        when(mapper.toResponse(occurrence)).thenReturn(response);

        OccurrenceResponse result = service.createOccurrence(request);

        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo(OccurrenceType.QUEIMADA);
        assertThat(result.getDescription()).isEqualTo("Test occurrence");

        verify(repository, times(1)).save(any(Occurrence.class));
        verify(mapper, times(1)).toEntity(request);
        verify(mapper, times(1)).toResponse(occurrence);
    }

    @Test
    void getOccurrenceById_WhenExists_ShouldReturnOccurrence() {
        UUID id = occurrence.getId();
        when(repository.findById(id)).thenReturn(Optional.of(occurrence));
        when(mapper.toResponse(occurrence)).thenReturn(response);

        OccurrenceResponse result = service.getOccurrenceById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);
        verify(repository, times(1)).findById(id);
    }

    @Test
    void getOccurrenceById_WhenNotExists_ShouldThrowException() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getOccurrenceById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Occurrence not found");

        verify(repository, times(1)).findById(id);
    }
}
