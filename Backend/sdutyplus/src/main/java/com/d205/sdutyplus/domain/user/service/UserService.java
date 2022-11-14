package com.d205.sdutyplus.domain.user.service;

import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import com.d205.sdutyplus.domain.statistics.entity.DailyTimeGraph;
import com.d205.sdutyplus.domain.statistics.repository.DailyStatisticsRepository;
import com.d205.sdutyplus.domain.statistics.repository.DailyTimeGraphRepository;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.user.dto.UserProfileDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.dto.UserRegResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.exception.NicknameAlreadyExistException;
import com.d205.sdutyplus.domain.user.repository.JobRepository;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.entity.Job;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.AuthUtils;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static com.d205.sdutyplus.global.error.ErrorCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final DailyTimeGraphRepository dailyTimeGraphRepository;
    private final AuthUtils authUtils;

    @Transactional
    public UserRegResponseDto userRegData(Long userSeq, UserRegDto userRegDto) {
        final User user = authUtils.getLoginUser(userSeq);

        if (userRepository.existsByNickname(userRegDto.getNickname())
            && !user.getNickname().equals(userRegDto.getNickname())) {
            throw new NicknameAlreadyExistException();
        }

        final Job job = jobRepository.findBySeq(userRegDto.getJob())
                .orElseThrow(() -> new EntityNotFoundException(JOB_NOT_FOUND));

        updateUserData(user, userRegDto, job);

        final DailyStatistics dailyStatistics = createUserStatisticsInfo(user, job);
        dailyStatisticsRepository.save(dailyStatistics);

        return new UserRegResponseDto(userRepository.findBySeq(userSeq).get());
    }

    @Transactional
    public boolean checkNicknameDuplicate(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public UserProfileDto getUserProfile(Long userSeq){
        final User user = authUtils.getLoginUser(userSeq);
        if (Period.between(user.getLastReport(), LocalDate.now()).getDays() >= 2) {
            updateContinuous(user, LocalDate.now(), 0);
        }

        List<DailyTimeGraph> dailyTimeGraphs = dailyTimeGraphRepository.findAll();

        final UserProfileDto result = new UserProfileDto(user, dailyTimeGraphs);
        return result;
    }

    @Transactional
    public void getReportContinuous(Long userSeq, TaskDto taskDto){
        final User user = authUtils.getLoginUser(userSeq);

        LocalDate today = TimeFormatter.StringToLocalDateTime(taskDto.getStartTime()).toLocalDate();
        LocalDate lastReport = user.getLastReport();
        int gap = Period.between(lastReport, today).getDays();

        long cnt = 1;

        if (user.getContinuous() > 0 && gap == 1) {
            cnt = user.getContinuous() + 1;
        }

        updateContinuous(user, today, cnt);
    }

    private void updateUserData(User user, UserRegDto userRegDto, Job job){
        user.setNickname(userRegDto.getNickname());
        user.setImgUrl(userRegDto.getImgUrl());
        user.setJob(job);
    }

    private void updateContinuous(User user, LocalDate date, long cnt){
        user.setLastReport(date);
        user.setContinuous(cnt);
    }

    private DailyStatistics createUserStatisticsInfo(User user, Job job){
        DailyStatistics result = new DailyStatistics();
        result.setUserSeq(user.getSeq());
        result.setJobSeq(job.getSeq());

        return result;
    }


}
