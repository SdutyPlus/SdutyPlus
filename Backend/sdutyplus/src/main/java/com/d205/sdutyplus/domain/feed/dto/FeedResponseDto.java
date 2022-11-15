package com.d205.sdutyplus.domain.feed.dto;

import com.d205.sdutyplus.domain.user.dto.UserWriterProfileDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedResponseDto {
    private Long seq;
    private UserWriterProfileDto writer;
    private String imgUrl;
    private String content;

    @QueryProjection
    public FeedResponseDto(Long seq, UserWriterProfileDto writer, String imgUrl, String content){
        this.seq = seq;
        this.writer = writer;
        this.imgUrl = imgUrl;
        this.content = content;
    }

    @QueryProjection
    public FeedResponseDto(Long seq, User writer, String imgUrl, String content){
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
    }
}
