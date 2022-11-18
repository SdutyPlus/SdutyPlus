package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.jwt.dto.JwtDto;
import com.d205.sdutyplus.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLoginDto {

    private Long userSeq;
    private String email;
    private String nickname;
    private String jobName;
    private String imgUrl;
    private String fcmToken;
    private JwtDto jwtDto;

    public UserLoginDto(User user, JwtDto jwtDto, String jobName) {
        this.userSeq = user.getSeq();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.jobName = jobName;
        this.imgUrl = user.getImgUrl();
        this.fcmToken = user.getFcmToken();
        this.jwtDto = jwtDto;
    }
}
