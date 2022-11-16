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

    @QueryProjection
    public FeedResponseDto(Long seq, UserWriterProfileDto writer, String imgUrl, String content, int feedLikesCount, int scrapCount){
        this.seq = seq;
        this.writer = writer;
        this.imgUrl = imgUrl;
        this.content = content;
        this.feedLikesCount = feedLikesCount;
        this.scrapCount = scrapCount;
    }
    @QueryProjection
    public FeedResponseDto(Long seq, Long writerSeq, String writerEmail, String writerNickname, String writerJobName, String writerImgUrl, String imgUrl, String content, int feedLikesCount, int scrapCount){
        this.seq = seq;
        this.writer = UserWriterProfileDto.builder()
                .userSeq(writerSeq)
                .email(writerEmail)
                .nickname(writerNickname)
                .job(writerJobName)
                .imgUrl(writerImgUrl)
                .build();
        this.imgUrl = imgUrl;
        this.content = content;
        this.feedLikesCount = feedLikesCount;
        this.scrapCount = scrapCount;
    }

    @QueryProjection
    public FeedResponseDto(Long seq, User writer, String imgUrl, String content, int feedLikesCount, int scrapCount){
        this.seq = seq;
        this.writer = UserWriterProfileDto.builder()
                .userSeq(writer.getSeq())
                .email(writer.getEmail())
                .nickname(writer.getNickname())
                .job(writer.getJob().getJobName())
                .imgUrl(writer.getImgUrl())
                .build();
        this.imgUrl = imgUrl;
        this.content = content;
        this.feedLikesCount = feedLikesCount;
        this.scrapCount = scrapCount;
    }
}
