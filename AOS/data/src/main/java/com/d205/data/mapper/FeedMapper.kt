package com.d205.data.mapper


import com.d205.data.model.feed.HomeFeedResponse
import com.d205.data.model.mypage.MyFeedResponse
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.model.mypage.Feed

//fun mapperFeedResponseToFeed(feedResponse: FeedResponse): Feed {
//    return Feed(
//        seq = feedResponse.seq,
//        writerSeq = feedResponse.writerSeq,
//        imgUrl = feedResponse.imgUrl,
//        thumbnail = feedResponse.thumbnail.toString(),
//        content = feedResponse.content.toString()
//    )
//}

fun mapperMyFeedResponseToFeed(myFeedResponse: MyFeedResponse): Feed {
    return Feed(
        seq = myFeedResponse.seq,
        writerSeq = myFeedResponse.writerSeq,
        imgUrl = myFeedResponse.feedImgUrl,
        content = myFeedResponse.content
    )
}

fun mapperHomeFeedsResponseToHomeFeeds(homeFeedsResponse: HomeFeedResponse): HomeFeed {
    return HomeFeed (
        seq = homeFeedsResponse.seq,
        writer = homeFeedsResponse.writer,
        imgUrl = homeFeedsResponse.imgUrl
    )
}