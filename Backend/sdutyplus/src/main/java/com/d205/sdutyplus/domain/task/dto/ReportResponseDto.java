package com.d205.sdutyplus.domain.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    private String totalTime;
    private List<TaskResponseDto> taskDtos;
}
