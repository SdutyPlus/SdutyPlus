package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class UserRegResponseDto {
    private Long seq;
    private String email;
    private String nickname;
    private Long job;
    private String imgUrl;
    private String fcmToken;

    public UserRegResponseDto(User user){
        this.seq = user.getSeq();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.job = user.getJob();
        this.imgUrl = user.getImgUrl();
        this.fcmToken = user.getFcmToken();
    }
}

