package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.jwt.dto.JwtDto;
import com.d205.sdutyplus.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginDto {

    private Long seq;
    private String email;
    private String nickname;
    private Long job;
    private String imaUrl;
    private String fcmToken;
    private JwtDto jwtDto;

    public UserLoginDto(User user, JwtDto jwtDto) {
        this.seq = user.getSeq();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.job = user.getJob();
        this.imaUrl = user.getImgUrl();
        this.fcmToken = user.getFcmToken();
        this.jwtDto = jwtDto;
    }
}
