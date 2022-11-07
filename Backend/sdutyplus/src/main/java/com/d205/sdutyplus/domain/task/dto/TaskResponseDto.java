package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.util.TimeFormatter;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDto {
    private Long seq;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
    private String startTime;
    private String endTime;
    private String content;
    private List<SubTaskResponseDto> subTaskDtos;
//    private List<SubTask> subTaskDtos;

    @QueryProjection
    public TaskResponseDto(Long seq, LocalDateTime startTime, LocalDateTime endTime, String content, List<SubTaskResponseDto> subtasks){
        this.seq = seq;
        this.startTime = TimeFormatter.LocalDateTimeToString(startTime);
        this.endTime = TimeFormatter.LocalDateTimeToString(endTime);
//        this.startTime = startTime;
//        this.endTime = endTime;
        this.content = content;
        if(subtasks.get(0).getSeq()==null){
            this.subTaskDtos = new ArrayList<>();
        }
        else{
            this.subTaskDtos = subtasks;
        }
    }
}
