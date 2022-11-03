package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.domain.task.entity.Task;
import lombok.Data;

@Data
public class TaskRequestDto {
    private String startTime;
    private String endTime;
    private int durationTime;
    private String content;

    public Task toEntity(){
        return Task.builder()
                .startTime(startTime)
                .endTime(endTime)
                .durationTime(durationTime)
                .content(content)
                .build();
    }
}
