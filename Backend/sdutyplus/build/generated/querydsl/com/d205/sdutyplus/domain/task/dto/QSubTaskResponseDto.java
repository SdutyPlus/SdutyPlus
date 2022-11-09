package com.d205.sdutyplus.domain.task.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.d205.sdutyplus.domain.task.dto.QSubTaskResponseDto is a Querydsl Projection type for SubTaskResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QSubTaskResponseDto extends ConstructorExpression<SubTaskResponseDto> {

    private static final long serialVersionUID = 1170970784L;

    public QSubTaskResponseDto(com.querydsl.core.types.Expression<Long> seq, com.querydsl.core.types.Expression<String> content) {
        super(SubTaskResponseDto.class, new Class<?>[]{long.class, String.class}, seq, content);
    }

}

