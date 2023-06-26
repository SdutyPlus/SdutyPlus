package com.d205.sdutyplus.util.domain.user;

import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.entity.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserUtils {

    public static User newInstance(){
        final StringBuilder sb = new StringBuilder();
        sb.append(RandomStringUtils.random(6, true, false))
                .append((int)(Math.random()*99) + 1)
                .append("@sduty.com");
        final String email = sb.toString();

        return of(email, SocialType.SDUTY);
    }

    public static User of(String email, SocialType socialType){
        return User.builder()
                .email(email)
                .socialType(socialType)
                .build();
    }
}
