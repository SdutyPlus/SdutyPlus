package com.d205.sdutyplus.domain.feed.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedResponseDto {
    public Long seq;
    public Long writerSeq;
    public String imgUrl;
    public String content;

    @QueryProjection
    public FeedResponseDto(Long seq, Long writerSeq, String imgUrl, String content){
        this.seq = seq;
        this.writerSeq = writerSeq;
        this.imgUrl = imgUrl;
        this.content = content;
    }
}
