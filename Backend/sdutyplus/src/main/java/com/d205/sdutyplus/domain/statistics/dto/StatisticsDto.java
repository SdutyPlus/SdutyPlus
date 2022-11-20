package com.d205.sdutyplus.domain.statistics.dto;

import com.d205.sdutyplus.domain.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StatisticsDto {
    @ApiModelProperty(value = "연속 일수")
    private Long continuous;
    @ApiModelProperty(value = "나의 공부 시간")
    private Long studyTime;
    @ApiModelProperty(value = "나의 퍼센테이지 확인")
    private Long studyPercent;
    @ApiModelProperty(value = "전체 인원의 공부 시간")
    private List<Long> dailyTimeGraphs;

    public StatisticsDto(User user, List<Long> dailyStatisticsList){
        this.continuous = user.getContinuous();
        this.studyTime = user.getStudyTime();
        this.studyPercent = user.getStudyPercent();
        this.dailyTimeGraphs = dailyStatisticsList;
    }
}
