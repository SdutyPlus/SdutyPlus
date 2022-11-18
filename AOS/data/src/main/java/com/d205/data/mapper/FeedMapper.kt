package com.d205.data.mapper


import com.d205.data.model.feed.FeedResponse
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.model.feed.Feed

//fun mapperFeedResponseToFeed(feedResponse: FeedResponse): Feed {
//    return Feed(
//        seq = feedResponse.seq,
//        writerSeq = feedResponse.writerSeq,
//        imgUrl = feedResponse.imgUrl,
//        thumbnail = feedResponse.thumbnail.toString(),
//        content = feedResponse.content.toString()
//    )
//}

fun mapperFeedResponseToFeed(feedResponse: FeedResponse): Feed {
    return Feed(
        seq = feedResponse.seq,
        writerSeq = feedResponse.writer.userSeq,
        feedImgUrl = feedResponse.imgUrl,
        content = feedResponse.content,
        profileImgUrl = feedResponse.writer.imgUrl,
        feedLikesCount = feedResponse.feedLikesCount,
        scrapCount = feedResponse.scrapCount,
        nickname = feedResponse.writer.nickname,
        feedScrapFlag = feedResponse.feedScrapFlag
    )
}

fun mapperFeedResponseToHomeFeeds(feedResponse: FeedResponse): HomeFeed {
    return HomeFeed (
        seq = feedResponse.seq,
        writer = feedResponse.writer,
        imgUrl = feedResponse.imgUrl,
        content = feedResponse.content,
        feedScrapFlag = feedResponse.feedScrapFlag
    )
}