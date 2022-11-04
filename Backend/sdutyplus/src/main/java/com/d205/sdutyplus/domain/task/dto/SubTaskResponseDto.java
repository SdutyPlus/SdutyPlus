package com.d205.sdutyplus.domain.task.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubTaskResponseDto {
    private Long seq;
    private String content;
}
