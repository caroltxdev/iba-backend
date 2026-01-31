package com.iba.dto.response;

import com.iba.domains.enums.OccurrenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeStatsResponse {
    private OccurrenceType type;
    private Long count;
}