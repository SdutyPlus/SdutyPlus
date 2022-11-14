package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.statistics.entity.DailyTimeGraph;
import com.d205.sdutyplus.domain.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;

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
    private String job;
    @ApiModelProperty(value = "프로필사진")
    private String imgUrl;
    @ApiModelProperty(value = "fcm token")
    private String fcmToken;
    @ApiModelProperty(value = "연속 일수")
    private Long continuous;
    @ApiModelProperty(value = "나의 공부 시간")
    private Long studyTime;
    @ApiModelProperty(value = "전체 인원의 공부 시간")
    private List<DailyTimeGraph> dailyTimeGraphs;

    public UserProfileDto(User user, List<DailyTimeGraph> dailyTimeGraphs){
        this.userSeq = user.getSeq();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.job = user.getJob().getJobName();
        this.imgUrl = user.getImgUrl();
        this.fcmToken = user.getFcmToken();
        this.continuous = user.getContinuous();
        this.studyTime = user.getStudyTime();
        this.dailyTimeGraphs = dailyTimeGraphs;
    }
}
