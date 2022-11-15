package com.d205.sdutyplus.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
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

    @QueryProjection
    public UserWriterProfileDto(Long userSeq, String email, String nickname, String job, String imgUrl){
        this.userSeq = userSeq;
        this.email = email;
        this.nickname = nickname;
        this.job = job;
        this.imgUrl = imgUrl;
    }
}
