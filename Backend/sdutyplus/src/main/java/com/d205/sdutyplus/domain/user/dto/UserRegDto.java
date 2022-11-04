package com.d205.sdutyplus.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRegDto {

    private String nickname;
    private String imgUrl;
    private Long job;
}
