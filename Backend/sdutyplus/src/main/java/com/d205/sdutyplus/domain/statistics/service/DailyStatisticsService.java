package com.d205.sdutyplus.domain.statistics.service;

import com.d205.sdutyplus.domain.statistics.dto.StatisticsDto;
import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import com.d205.sdutyplus.domain.statistics.entity.DailyTimeGraph;
import com.d205.sdutyplus.domain.statistics.repository.DailyStatisticsRepository;
import com.d205.sdutyplus.domain.statistics.repository.DailyTimeGraphRepository;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.AuthUtils;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.d205.sdutyplus.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class DailyStatisticsService {

    private final AuthUtils authUtils;
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final DailyTimeGraphRepository dailyTimeGraphRepository;
    private final UserRepository userRepository;

    @Transactional
    public StatisticsDto getUserStatistics(){
        final User user = authUtils.getLoginUser();

        if (Period.between(user.getLastReport(), LocalDate.now()).getDays() >= 2) {
            updateContinuous(user, LocalDate.now(), 0);
        }

        List<DailyTimeGraph> dailyTimeGraphs = dailyTimeGraphRepository.findAll();
        List<Long> timeList = new ArrayList<>();
        for (DailyTimeGraph dailyTimeGraph : dailyTimeGraphs) {
            timeList.add(dailyTimeGraph.getCount());
        }

        final StatisticsDto statisticsDto = new StatisticsDto(user, timeList);
        return statisticsDto;
    }

    @Transactional
    public void getReportContinuous(TaskDto taskDto){
        final User user = authUtils.getLoginUser();

        LocalDate today = TimeFormatter.StringToLocalDateTime(taskDto.getStartTime()).toLocalDate();
        LocalDate lastReport = user.getLastReport();
        int gap = Period.between(lastReport, today).getDays();

        if (gap > 0) {
            long cnt = 1;

            if (user.getContinuous() > 0 && gap == 1) {
                cnt = user.getContinuous() + 1;
            }

            updateContinuous(user, today, cnt);
        }
    }

    @Transactional
    public void updateDailyStudy(TaskDto taskDto){
        final Long userSeq = authUtils.getLoginUserSeq();

        DailyStatistics dailyStatistics = dailyStatisticsRepository.findByUserSeq(userSeq)
                .orElseThrow(() -> new EntityNotFoundException(STATISTICS_NOT_FOUND));

        LocalTime startTime = TimeFormatter.StringToLocalDateTime(taskDto.getStartTime()).toLocalTime();
        LocalTime endTime = TimeFormatter.StringToLocalDateTime(taskDto.getEndTime()).toLocalTime();

        long studyTime = ChronoUnit.HOURS.between(startTime, endTime);
        long studyMinute = ChronoUnit.MINUTES.between(startTime, endTime) - studyTime * 60;

        dailyStatistics.plusStudyTime(studyTime, studyMinute);
    }

    @Transactional
    @Scheduled(cron = "0 0 15 * * *")
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
        dailyStatisticsRepository.resetMinute();
    }

    private void updateContinuous(User user, LocalDate date, long cnt){
        user.setLastReport(date);
        user.setContinuous(cnt);
    }
}