package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.util.TimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReportDto {
    private String totalTime;
    private int percentage;
    private List<TaskDto> taskDtos;

    public ReportDto(List<TaskDto> taskDtos){
        int totalSecond = 0;
        for(TaskDto taskDto : taskDtos){
            LocalDateTime startTime = TimeFormatter.StringToLocalDateTime(taskDto.getStartTime());
            LocalDateTime endTime = TimeFormatter.StringToLocalDateTime(taskDto.getEndTime());
            totalSecond+=TimeFormatter.getDurationTime(startTime, endTime);
        }
        this.percentage = Math.min(totalSecond*100/7200000, 100);
        this.totalTime = TimeFormatter.msToTime(totalSecond);
        this.taskDtos = taskDtos;
    }
}
