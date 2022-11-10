package com.d205.sdutyplus.domain.statistics.repository;

import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics, Long> {
    Optional<DailyStatistics> findByUserSeq(Long userSeq);

    @Query(value = "UPDATE DailyStatistics SET daily_study_time=0")
    void resetTime();

    void deleteByUserSeq(Long userSeq);
}
