package com.d205.sdutyplus.domain.feed.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedResponseDto {
    private Long seq;
    private Long writerSeq;
    private String imgUrl;
    private String content;

    @QueryProjection
    public FeedResponseDto(Long seq, Long writerSeq, String imgUrl, String content){
        this.seq = seq;
        this.writerSeq = writerSeq;
        this.imgUrl = imgUrl;
        this.content = content;
    }
}
