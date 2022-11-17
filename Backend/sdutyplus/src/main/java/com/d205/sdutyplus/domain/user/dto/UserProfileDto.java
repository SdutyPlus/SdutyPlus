package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.statistics.entity.DailyTimeGraph;
import com.d205.sdutyplus.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileDto {

    @ApiModelProperty(value = "유저 번호")
    private Long userSeq;
    @ApiModelProperty(value = "유저 이메일")
    private String email;
    @ApiModelProperty(value = "유저 별명", example = "Faker")
    private String nickname;
    @ApiModelProperty(value = "유저 직업")
    private String jobName;
    @ApiModelProperty(value = "프로필사진")
    private String imgUrl;
    @ApiModelProperty(value = "fcm token")
    private String fcmToken;

    public UserProfileDto(User user){
        this.userSeq = user.getSeq();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.jobName = user.getJob().getJobName();
        this.imgUrl = user.getImgUrl();
        this.fcmToken = user.getFcmToken();
    }
}
