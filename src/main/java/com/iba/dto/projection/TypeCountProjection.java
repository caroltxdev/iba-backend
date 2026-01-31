package com.iba.dto.projection;

import com.iba.domains.enums.OccurrenceType;

public interface TypeCountProjection {
    OccurrenceType getType();
    Long getCount();
}