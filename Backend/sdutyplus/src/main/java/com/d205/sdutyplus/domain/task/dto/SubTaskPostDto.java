package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.domain.task.entity.SubTask;
import lombok.Data;

@Data
public class SubTaskPostDto {
    public Long taskSeq;
    public String content;

    public SubTask toEntity(){
        return SubTask.builder()
                .taskSeq(taskSeq)
                .content(content)
                .build();
    }
}
