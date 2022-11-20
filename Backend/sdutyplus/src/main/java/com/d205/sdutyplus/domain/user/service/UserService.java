package com.d205.sdutyplus.domain.user.service;

import com.d205.sdutyplus.domain.user.dto.UserProfileDto;
import com.d205.sdutyplus.domain.user.dto.UserProfileEditDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.dto.UserRegResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.exception.NicknameAlreadyExistException;
import com.d205.sdutyplus.domain.user.repository.JobRepository;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.entity.Job;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.d205.sdutyplus.global.error.ErrorCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final AuthUtils authUtils;

    @Transactional
    public UserRegResponseDto userRegData(UserRegDto userRegDto) {
        final User user = authUtils.getLoginUser();

        if (userRepository.existsByNickname(userRegDto.getNickname())
                && !user.getNickname().equals(userRegDto.getNickname())) {
            throw new NicknameAlreadyExistException();
        }

        final Job job = getJob(userRegDto.getJobName());

        setUserRegData(user, userRegDto, job);

        return new UserRegResponseDto(user);
    }


    @Transactional
    public UserRegResponseDto userProfileEdit(UserProfileEditDto userProfileEditDto) {
        final User user = authUtils.getLoginUser();

        if (userRepository.existsByNickname(userProfileEditDto.getNickname())
                && !user.getNickname().equals(userProfileEditDto.getNickname())) {
            throw new NicknameAlreadyExistException();
        }

        final Job job = getJob(userProfileEditDto.getJobName());

        setUserData(user, userProfileEditDto, job);

        return new UserRegResponseDto(user);
    }

    public boolean checkNicknameDuplicate(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    public UserProfileDto getUserProfile(){
        final User user = authUtils.getLoginUser();

        final UserProfileDto result = new UserProfileDto(user);
        return result;
    }

    private void setUserRegData(User user, UserRegDto userRegDto, Job job){
        user.setNickname(userRegDto.getNickname());
        user.setImgUrl(userRegDto.getImgUrl());
        user.setJob(job);
    }

    private void setUserData(User user, UserProfileEditDto userProfileEditDto, Job job){
        user.setNickname(userProfileEditDto.getNickname());
        user.setImgUrl(userProfileEditDto.getImgUrl());
        user.setJob(job);
    }

    private Job getJob(String jobName){
        return jobRepository.findByJobName(jobName)
                .orElseThrow(() -> new EntityNotFoundException(JOB_NOT_FOUND));
    }
}
