package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.util.TimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    private String totalTime;
    private List<TaskResponseDto> taskDtos;

    public ReportResponseDto(List<TaskResponseDto> taskDtos){
        int totalSecond = 0;
        for(TaskResponseDto taskResponseDto : taskDtos){
            LocalDateTime startTime = TimeFormatter.StringToLocalDateTime(taskResponseDto.getStartTime());
            LocalDateTime endTime = TimeFormatter.StringToLocalDateTime(taskResponseDto.getEndTime());
            Duration duration = Duration.between(endTime, startTime);
            totalSecond+=(int)duration.getSeconds();
        }
        this.totalTime = TimeFormatter.msToTime(totalSecond);
        this.taskDtos = taskDtos;
    }
}
