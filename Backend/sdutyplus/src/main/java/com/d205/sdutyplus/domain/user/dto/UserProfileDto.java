package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Getter
@NoArgsConstructor
public class UserProfileDto {

    @ApiModelProperty(value = "유저 번호")
    private Long userSeq;
    @ApiModelProperty(value = "유저 이메일")
    private String email;
    @ApiModelProperty(value = "유저 별명", example = "Faker")
    private String nickname;
    @ApiModelProperty(value = "유저 직업")
    private Long job;
    @ApiModelProperty(value = "프로필사진")
    private String imgUrl;
    @ApiModelProperty(value = "fcm token")
    private String fcmToken;

    public UserProfileDto(User user){
        this.userSeq = user.getSeq();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.job = user.getJob();
        this.imgUrl = user.getImgUrl();
        this.fcmToken = user.getFcmToken();
    }
}
