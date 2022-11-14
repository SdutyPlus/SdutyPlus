package com.d205.sdutyplus.domain.warn.dto;

import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WarnUserDto {
    private Long seq;
    private String email;
    private SocialType socialType;
    private String nickname;
    private String imgUrl;
    private boolean delYN;
    private boolean banYN;

    @QueryProjection
    public WarnUserDto(Long seq, String email, SocialType socialType, String nickname, String imgUrl, Boolean delYN, Boolean banYN) {
        this.seq = seq;
        this.email = email;
        this.socialType = socialType;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.delYN = delYN;
        this.banYN = banYN;
    }
}
