package com.d205.sdutyplus.domain.statistics.service;

import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import com.d205.sdutyplus.domain.statistics.entity.DailyTimeGraph;
import com.d205.sdutyplus.domain.statistics.repository.DailyStatisticsRepository;
import com.d205.sdutyplus.domain.statistics.repository.DailyTimeGraphRepository;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.d205.sdutyplus.global.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyStatisticsService {

    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final DailyTimeGraphRepository dailyTimeGraphRepository;
    private final UserRepository userRepository;

    @Transactional
    public void updateDailyStudy(Long userSeq, TaskDto taskDto){
        DailyStatistics dailyStatistics = dailyStatisticsRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new EntityNotFoundException(STATISTICS_NOT_FOUND));

        LocalTime startTime = TimeFormatter.StringToLocalDateTime(taskDto.getStartTime()).toLocalTime();
        LocalTime endTime = TimeFormatter.StringToLocalDateTime(taskDto.getEndTime()).toLocalTime();

        Long studyTime = ChronoUnit.HOURS.between(startTime, endTime);

        dailyStatistics.plusStudyTime(studyTime);
    }

    @Transactional
    @Scheduled(cron = "0 5 11 * * *")
    public void resetDailyTime(){
        List<DailyTimeGraph> dailyTimeGraphs = dailyTimeGraphRepository.findAll();

        long reset = 0;

        for (int time = 0; time < 5; time++) {
            dailyTimeGraphs.get(time).setCount(reset);
        }

        for (int time = 0; time < 3; time++) {
            dailyTimeGraphs.get(time).counter(dailyStatisticsRepository.countByDailyStudyTime(time * 2 + 1));
            dailyTimeGraphs.get(time).counter(dailyStatisticsRepository.countByDailyStudyTime(time * 2 + 2));
        }

        for (int time = 8; time < 25; time++)  {
            dailyTimeGraphs.get(4).counter(dailyStatisticsRepository.countByDailyStudyTime(time));
        }

        List<User> users = userRepository.findAll();

        for (User user : users) {
            DailyStatistics dailyStatistics = dailyStatisticsRepository.findByUserSeq(user.getSeq()).get();
            user.setStudyTime(dailyStatistics.getDailyStudyTime());
        }

        dailyStatisticsRepository.resetTime();
    }
}
