package com.d205.sdutyplus.domain.task.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.d205.sdutyplus.domain.task.dto.QTaskResponseDto is a Querydsl Projection type for TaskResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QTaskResponseDto extends ConstructorExpression<TaskResponseDto> {

    private static final long serialVersionUID = -1144029038L;

    public QTaskResponseDto(com.querydsl.core.types.Expression<Long> seq, com.querydsl.core.types.Expression<java.time.LocalDateTime> startTime, com.querydsl.core.types.Expression<java.time.LocalDateTime> endTime, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<? extends java.util.List<SubTaskResponseDto>> subtasks) {
        super(TaskResponseDto.class, new Class<?>[]{long.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class, String.class, java.util.List.class}, seq, startTime, endTime, content, subtasks);
    }

}

