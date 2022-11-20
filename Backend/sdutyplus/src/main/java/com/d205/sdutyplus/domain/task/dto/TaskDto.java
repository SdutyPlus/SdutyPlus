package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskDto {
    private Long seq;
    private String startTime;
    private String endTime;
    private String title;
    private List<String> contents;

    public TaskDto(Task task, List<String> contents){
        this.seq = task.getSeq();
        this.startTime = TimeFormatter.LocalDateTimeToString(task.getStartTime());
        this.endTime = TimeFormatter.LocalDateTimeToString(task.getEndTime());
        this.title = task.getTitle();
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
