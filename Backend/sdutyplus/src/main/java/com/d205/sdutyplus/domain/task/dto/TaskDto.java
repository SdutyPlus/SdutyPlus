package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class TaskDto {
    private Long seq;
    private String startTime;
    private String endTime;
    private String title;
    private List<String> contents;

    @Builder
    public TaskDto(Long seq, LocalDateTime startTime, LocalDateTime endTime, String title, List<String> contents){
        this.seq = seq;
        this.startTime = TimeFormatter.LocalDateTimeToString(startTime);
        this.endTime = TimeFormatter.LocalDateTimeToString(endTime);
        this.title = title;
        this.contents = contents;
    }

    public Task toEntity(){
        return Task.builder()
                .startTime(startTime)
                .endTime(endTime)
                .title(title)
                .build();
    }

}
