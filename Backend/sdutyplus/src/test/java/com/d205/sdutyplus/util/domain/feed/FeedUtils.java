package com.d205.sdutyplus.util.domain.feed;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.user.entity.User;

public class FeedUtils {

    public static Feed newInstance(User user){
        return of(user, "testImgUrl", "testContent");
    }

    public static Feed of(User user, String imgUrl, String content){
        return Feed.builder()
                .writer(user)
                .imgUrl(imgUrl)
                .content(content)
                .build();
    }
}
