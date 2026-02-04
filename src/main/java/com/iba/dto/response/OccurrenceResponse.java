package com.iba.dto.response;

import com.iba.domains.enums.OccurrenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OccurrenceResponse {
    private UUID id;
    private OccurrenceType type;
    private LocalDate date;
    private String description;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String photoUrl;
    private Instant createdAt;
}