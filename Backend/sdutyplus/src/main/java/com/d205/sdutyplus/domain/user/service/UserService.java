package com.d205.sdutyplus.domain.user.service;

import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.user.dto.UserProfileDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.dto.UserRegResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.exception.NicknameAlreadyExistException;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.Period;

import static com.d205.sdutyplus.global.error.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    @Transactional
    public UserRegResponseDto userRegData(Long userSeq, UserRegDto userRegDto) {
        final User user = authUtils.getLoginUser(userSeq);

        if (userRepository.existsByNickname(userRegDto.getNickname())
            && !user.getNickname().equals(userRegDto.getNickname())) {
            throw new NicknameAlreadyExistException();
        }

        updateUserData(user, userRegDto);

        return new UserRegResponseDto(userRepository.findBySeq(userSeq).get());
    }

    @Transactional
    public boolean checkNicknameDuplicate(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public UserProfileDto getUserProfile(Long userSeq){
        final User user = authUtils.getLoginUser(userSeq);

        final UserProfileDto result = new UserProfileDto(user);

        return result;
    }

    @Transactional
    public void getReportContinuous(Long userSeq, Task task){
        final User user = authUtils.getLoginUser(userSeq);

        LocalDate today = task.getStartTime().toLocalDate();
        LocalDate lastReport = user.getLastReport();
        int gap = Period.between(lastReport, today).getDays();

        if (user.getContinuous() > 0) {
            if (gap == 1) {
                updateContinuous(user, today, user.getContinuous() + 1);
            } else if (gap >= 2) {
                updateContinuous(user, today, 1);
            }
        } else {
            updateContinuous(user, today, 1);
        }
    }

    private void updateUserData(User user, UserRegDto userRegDto){
        user.setNickname(userRegDto.getNickname());
        user.setImgUrl(userRegDto.getImgUrl());
        user.setJob(userRegDto.getJob());
    }

    private void updateContinuous(User user, LocalDate date, long cnt){
        user.setLastReport(date);
        user.setContinuous(cnt);
    }
}
