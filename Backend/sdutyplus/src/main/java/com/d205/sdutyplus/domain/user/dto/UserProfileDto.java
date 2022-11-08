package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor
public class UserProfileDto {

    private Long userSeq;
    private String nickname;
    private Long job;
    private String imgUrl;

    public UserProfileDto(User user){
        this.userSeq = user.getSeq();
        this.nickname = user.getNickname();
        this.job = user.getJob();
        this.imgUrl = user.getImgUrl();
    }
}
