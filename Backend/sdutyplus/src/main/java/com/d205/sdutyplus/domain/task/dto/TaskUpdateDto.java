package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.domain.task.entity.Task;

public class TaskUpdateDto {
    public String startTime;
    public String endTime;
    public String content;

    public Task toEntity(){
        return Task.builder()
                .startTime(startTime)
                .endTime(endTime)
                .content(content)
                .build();
    }
}
