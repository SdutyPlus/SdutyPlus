package com.d205.sdutyplus.domain.user.dto;

import com.d205.sdutyplus.domain.user.entity.User;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWriterProfileDto {
    @ApiModelProperty(value = "유저 번호")
    private Long userSeq;
    @ApiModelProperty(value = "유저 이메일")
    private String email;
    @ApiModelProperty(value = "유저 별명", example = "Faker")
    private String nickname;
    @ApiModelProperty(value = "유저 직업")
    private String job;
    @ApiModelProperty(value = "프로필사진")
    private String imgUrl;

    public UserWriterProfileDto(User writer){
        this.userSeq = writer.getSeq();
        this.email = writer.getEmail();
        this.nickname = writer.getNickname();
        this.job = writer.getJob().getJobName();
        this.imgUrl = writer.getImgUrl();
    }
}
