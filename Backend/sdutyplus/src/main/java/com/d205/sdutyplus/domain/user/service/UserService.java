package com.d205.sdutyplus.domain.user.service;

import com.d205.sdutyplus.domain.user.dto.UserDto;
import com.d205.sdutyplus.domain.user.dto.UserProfileDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.exception.NicknameAlreadyExistException;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.d205.sdutyplus.global.error.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void userRegData(Long userSeq, UserRegDto userRegDto) {
        final User user = userRepository.findBySeq(userSeq).get();

        if (userRepository.existsByNickname(userRegDto.getNickname())
            && !user.getNickname().equals(userRegDto.getNickname())) {
            throw new NicknameAlreadyExistException();
        }

        updateUserData(user, userRegDto);
    }

    @Transactional
    public boolean checkNicknameDuplicate(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }

    @Transactional
    public UserProfileDto getUserProfile(Long userSeq){
        final User user = userRepository.findBySeq(userSeq)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        final UserProfileDto result = new UserProfileDto(user);

        return result;
    }

    private void updateUserData(User user, UserRegDto userRegDto){
        user.setNickname(userRegDto.getNickname());
        user.setImgUrl(userRegDto.getImgUrl());
        user.setJob(userRegDto.getJob());
    }
}
