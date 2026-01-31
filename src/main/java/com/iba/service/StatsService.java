package com.iba.service;

import com.iba.dto.projection.MonthCountProjection;
import com.iba.dto.projection.TypeCountProjection;
import com.iba.dto.response.MonthStatsResponse;
import com.iba.dto.response.SummaryStatsResponse;
import com.iba.dto.response.TypeStatsResponse;
import com.iba.repository.OccurrenceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsService {

    private final OccurrenceRepository repository;

    @Transactional(readOnly = true)
    public SummaryStatsResponse getSummaryStats(LocalDate start, LocalDate end) {
        log.info("Generating summary stats: start={}, end={}", start, end);

        Long total = repository.countTotal(start, end);

        List<TypeStatsResponse> byType = repository.countByType(start, end).stream()
                .map(projection -> TypeStatsResponse.builder()
                        .type(projection.getType())
                        .count(projection.getCount())
                        .build())
                .collect(Collectors.toList());

        List<MonthStatsResponse> byMonth = repository.countByMonth(start, end).stream()
                .map(projection -> MonthStatsResponse.builder()
                        .month(projection.getMonth())
                        .count(projection.getCount())
                        .build())
                .collect(Collectors.toList());

        log.info("Stats generated: total={}, typeGroups={}, monthGroups={}",
                total, byType.size(), byMonth.size());

        return SummaryStatsResponse.builder()
                .total(total)
                .byType(byType)
                .byMonth(byMonth)
                .build();
    }
}