package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.domain.task.entity.Task;
import lombok.Data;

import java.util.List;

@Data
public class TaskDto {
    private Long seq;
    private String startTime;
    private String endTime;
    private String title;
    private List<String> contents;

    public Task toEntity(){
        return Task.builder()
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .build();
    }

}
