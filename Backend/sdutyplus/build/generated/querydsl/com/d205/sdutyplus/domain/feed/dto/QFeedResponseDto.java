package com.d205.sdutyplus.domain.feed.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.d205.sdutyplus.domain.feed.dto.QFeedResponseDto is a Querydsl Projection type for FeedResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QFeedResponseDto extends ConstructorExpression<FeedResponseDto> {

    private static final long serialVersionUID = -982343566L;

    public QFeedResponseDto(com.querydsl.core.types.Expression<Long> seq, com.querydsl.core.types.Expression<Long> writerSeq, com.querydsl.core.types.Expression<String> imgUrl, com.querydsl.core.types.Expression<String> content) {
        super(FeedResponseDto.class, new Class<?>[]{long.class, long.class, String.class, String.class}, seq, writerSeq, imgUrl, content);
    }

}

