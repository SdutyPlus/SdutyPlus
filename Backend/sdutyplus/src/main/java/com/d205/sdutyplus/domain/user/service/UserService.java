package com.d205.sdutyplus.domain.user.service;

import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import com.d205.sdutyplus.domain.statistics.entity.DailyTimeGraph;
import com.d205.sdutyplus.domain.statistics.repository.DailyStatisticsRepository;
import com.d205.sdutyplus.domain.statistics.repository.DailyTimeGraphRepository;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.user.dto.UserProfileDto;
import com.d205.sdutyplus.domain.user.dto.UserProfileEditDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.dto.UserRegResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.exception.NicknameAlreadyExistException;
import com.d205.sdutyplus.domain.user.exception.UserNotLoginException;
import com.d205.sdutyplus.domain.user.repository.JobRepository;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.entity.Job;
import com.d205.sdutyplus.global.error.ErrorResponseDto;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.AuthUtils;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static com.d205.sdutyplus.global.error.ErrorCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final DailyTimeGraphRepository dailyTimeGraphRepository;
    private final AuthUtils authUtils;

    @Transactional
    public UserRegResponseDto userRegData(UserRegDto userRegDto) {
        final Long userSeq = authUtils.getLoginUserSeq();
        final User user = authUtils.getLoginUser(userSeq);

        if (userRepository.existsByNickname(userRegDto.getNickname())
                && !user.getNickname().equals(userRegDto.getNickname())) {
            throw new NicknameAlreadyExistException();
        }

        final Job job = jobRepository.findByJobName("기타")
                .orElseThrow(() -> new EntityNotFoundException(JOB_NOT_FOUND));

        regUserData(user, userRegDto, job);

        return new UserRegResponseDto(userRepository.findBySeq(userSeq).get());
    }


    @Transactional
    public UserRegResponseDto userProfileEdit(UserProfileEditDto userProfileEditDto) {
        final Long userSeq = authUtils.getLoginUserSeq();
        final User user = authUtils.getLoginUser(userSeq);

        if (userRepository.existsByNickname(userProfileEditDto.getNickname())
                && !user.getNickname().equals(userProfileEditDto.getNickname())) {
            throw new NicknameAlreadyExistException();
        }

        final Job job = jobRepository.findByJobName("기타")
                .orElseThrow(() -> new EntityNotFoundException(JOB_NOT_FOUND));

        updateUserData(user, userProfileEditDto, job);

        return new UserRegResponseDto(userRepository.findBySeq(userSeq).get());
    }

    @Transactional
    public boolean checkNicknameDuplicate(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public UserProfileDto getUserProfile(){
        final Long userSeq = authUtils.getLoginUserSeq();
        final User user = authUtils.getLoginUser(userSeq);

        final UserProfileDto result = new UserProfileDto(user);
        return result;
    }

    private void regUserData(User user, UserRegDto userRegDto, Job job){
        user.setNickname(userRegDto.getNickname());
        user.setImgUrl(userRegDto.getImgUrl());
        user.setJob(job);
    }


    private void updateUserData(User user, UserProfileEditDto userProfileEditDto, Job job){
        user.setNickname(userProfileEditDto.getNickname());
        user.setImgUrl(userProfileEditDto.getImgUrl());
        user.setJob(job);
    }
}
