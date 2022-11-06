package com.d205.sdutyplus.domain.task.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubTaskResponseDto {
    private Long seq;
    private String content;

    @QueryProjection
    public SubTaskResponseDto(Long seq, String content){
        this.seq = seq;
        this.content = content;
    }
}
