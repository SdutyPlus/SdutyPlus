package com.d205.sdutyplus.domain.statistics.repository;

import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics, Long> {
    Optional<DailyStatistics> findByUserSeq(Long userSeq);
}
