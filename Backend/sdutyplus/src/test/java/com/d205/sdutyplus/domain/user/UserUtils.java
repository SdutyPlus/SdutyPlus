package com.d205.sdutyplus.domain.user;

import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.entity.User;

public class UserUtils {

    public static User of(String email, SocialType socialType){
        return User.builder()
                .email(email)
                .socialType(socialType)
                .build();
    }
}
