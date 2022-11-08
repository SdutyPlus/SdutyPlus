package com.d205.sdutyplus.domain.task.dto;

import com.d205.sdutyplus.domain.task.entity.SubTask;
import lombok.Data;

@Data
public class SubTaskRequestDto {
    private Long taskSeq;
    private String content;

    public SubTask toEntity(){
        return SubTask.builder()
                .taskSeq(taskSeq)
                .content(content)
                .build();
    }
}
