package com.iba.dto.mapper;

import com.iba.domain.entity.Occurrence;
import com.iba.dto.request.CreateOccurrenceRequest;
import com.iba.dto.response.OccurrenceResponse;
import org.springframework.stereotype.Component;

@Component
public class OccurrenceMapper {

    public Occurrence toEntity(CreateOccurrenceRequest request) {
        return Occurrence.builder()
                .type(request.getType())
                .date(request.getDate())
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .photoUrl(request.getPhotoUrl())
                .build();
    }

    public OccurrenceResponse toResponse(Occurrence occurrence) {
        return OccurrenceResponse.builder()
                .id(occurrence.getId())
                .type(occurrence.getType())
                .date(occurrence.getDate())
                .description(occurrence.getDescription())
                .latitude(occurrence.getLatitude())
                .longitude(occurrence.getLongitude())
                .photoUrl(occurrence.getPhotoUrl())
                .createdAt(occurrence.getCreatedAt())
                .build();
    }
}