package com.d205.sdutyplus.domain.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private Long seq;
    private String startTime;
    private String endTime;
    private String content;
    private List<SubTaskResponseDto> subTaskDtos;
}
