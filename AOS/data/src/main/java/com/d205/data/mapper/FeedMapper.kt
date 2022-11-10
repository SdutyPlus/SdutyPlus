package com.d205.data.mapper

import com.d205.data.model.mypage.FeedResponse
import com.d205.domain.model.mypage.Feed

fun mapperFeedResponseToFeed(feedResponse: FeedResponse): Feed {
    return Feed(
        seq = feedResponse.seq,
        writerSeq = feedResponse.writerSeq,
        imgUrl = feedResponse.imageSource,
        thumbnail = feedResponse.thumbnail,
        jobHashtag = feedResponse.jobHashtag,
        contents = feedResponse.contents,
        regtime = feedResponse.regtime,
        feedPublic = feedResponse.storyPublic,
        feedWarning = feedResponse.storyWarning
    )
}