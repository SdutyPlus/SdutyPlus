package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDto {
    private Long seq;
    private String email;
    private String nickname;
    private String job;
    private String imgUrl;
    private String fcmToken;

    public UserDto(User user) {
        this.seq = user.getSeq();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.job = user.getJob().getJobName();
        this.imgUrl = user.getImgUrl();
        this.fcmToken = user.getFcmToken();
    }
}
