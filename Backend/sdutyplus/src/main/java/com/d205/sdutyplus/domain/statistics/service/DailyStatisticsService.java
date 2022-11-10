package com.d205.sdutyplus.domain.statistics.service;

import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import com.d205.sdutyplus.domain.statistics.repository.DailyStatisticsRepository;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static com.d205.sdutyplus.global.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyStatisticsService {

    private final DailyStatisticsRepository dailyStatisticsRepository;

    @Transactional
    public void updateDailyStudy(Long userSeq, TaskDto taskDto){
        DailyStatistics dailyStatistics = dailyStatisticsRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new EntityNotFoundException(STATISTICS_NOT_FOUND));

        LocalTime startTime = TimeFormatter.StringToLocalDateTime(taskDto.getStartTime()).toLocalTime();
        LocalTime endTime = TimeFormatter.StringToLocalDateTime(taskDto.getEndTime()).toLocalTime();

        Long studyTime = ChronoUnit.HOURS.between(startTime, endTime);

        dailyStatistics.plusStudyTime(studyTime);
    }
}
