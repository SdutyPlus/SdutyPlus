package com.d205.sdutyplus.domain.user.service;

import com.d205.sdutyplus.domain.user.dto.UserDto;
import com.d205.sdutyplus.domain.user.dto.UserLoginDto;
import com.d205.sdutyplus.domain.user.dto.UserProfileDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserDto userRegData(Long userSeq, UserRegDto userRegDto) {
        User user = userRepository.findBySeq(userSeq).get();

        user.setNickname(userRegDto.getNickname());
        user.setImgUrl(userRegDto.getImgUrl());
        user.setJob(userRegDto.getJob());

        UserDto userDto = new UserDto(user);

        return userDto;
    }

    @Transactional
    public boolean checkNicknameDuplicate(String nickname) {

        return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public UserProfileDto getUserProfile(Long userSeq){
        User user = userRepository.findBySeq(userSeq).get();
        UserProfileDto userProfileDto = new UserProfileDto(user);

        return userProfileDto;
    }
}
