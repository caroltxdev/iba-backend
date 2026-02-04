package com.iba.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryStatsResponse {
    private Long total;
    private List<TypeStatsResponse> byType;
    private List<MonthStatsResponse> byMonth;
}