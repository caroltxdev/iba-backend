package com.iba.repository;

import com.iba.domain.entity.Occurrence;
import com.iba.domains.enums.OccurrenceType;
import com.iba.dto.projection.MonthCountProjection;
import com.iba.dto.projection.TypeCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface OccurrenceRepository extends JpaRepository<Occurrence, UUID> {

    @Query(value = "SELECT * FROM occurrence o WHERE " +
            "(:type IS NULL OR o.type = CAST(:type AS VARCHAR)) AND " +
            "(CAST(:start AS DATE) IS NULL OR o.date >= CAST(:start AS DATE)) AND " +
            "(CAST(:end AS DATE) IS NULL OR o.date <= CAST(:end AS DATE)) " +
            "ORDER BY o.date DESC, o.created_at DESC",
            nativeQuery = true)
    List<Occurrence> findFiltered(
            @Param("type") String type,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query(value = "SELECT o.type as type, COUNT(o.id) as count FROM occurrence o WHERE " +
            "(CAST(:start AS DATE) IS NULL OR o.date >= CAST(:start AS DATE)) AND " +
            "(CAST(:end AS DATE) IS NULL OR o.date <= CAST(:end AS DATE)) " +
            "GROUP BY o.type",
            nativeQuery = true)
    List<TypeCountProjection> countByType(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query(value = "SELECT TO_CHAR(DATE_TRUNC('month', date), 'YYYY-MM') as month, COUNT(*) as count " +
            "FROM occurrence " +
            "WHERE (CAST(:start AS DATE) IS NULL OR date >= CAST(:start AS DATE)) " +
            "AND (CAST(:end AS DATE) IS NULL OR date <= CAST(:end AS DATE)) " +
            "GROUP BY DATE_TRUNC('month', date) " +
            "ORDER BY DATE_TRUNC('month', date)",
            nativeQuery = true)
    List<MonthCountProjection> countByMonth(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query(value = "SELECT COUNT(o.id) FROM occurrence o WHERE " +
            "(CAST(:start AS DATE) IS NULL OR o.date >= CAST(:start AS DATE)) AND " +
            "(CAST(:end AS DATE) IS NULL OR o.date <= CAST(:end AS DATE))",
            nativeQuery = true)
    Long countTotal(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );
}