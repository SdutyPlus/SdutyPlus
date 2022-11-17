package com.d205.sdutyplus.domain.feed.dto;

import com.d205.sdutyplus.domain.user.dto.UserWriterProfileDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
public class FeedResponseDto {
    private Long seq;
    private UserWriterProfileDto writer;
    private String imgUrl;
    private String content;
    private int feedLikesCount;
    private int scrapCount;
    private boolean feedLikeFlag;
    private boolean feedScrapFlag;

    @QueryProjection
    public FeedResponseDto(Long seq, User writer, String imgUrl, String content, int feedLikesCount,
                           int scrapCount, boolean feedLikeFlag, boolean feedScrapFlag){
        this.seq = seq;
        this.writer = new UserWriterProfileDto(writer);
        this.imgUrl = imgUrl;
        this.content = content;
        this.feedLikesCount = feedLikesCount;
        this.scrapCount = scrapCount;
        this.feedLikeFlag = feedLikeFlag;
        this.feedScrapFlag = feedScrapFlag;
    }
}
