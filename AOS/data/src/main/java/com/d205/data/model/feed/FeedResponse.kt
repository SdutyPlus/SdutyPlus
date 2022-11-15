package com.d205.data.model.feed

import com.d205.domain.model.feed.Writer

data class FeedResponse(
    val seq: Int = 0,
    val writer: Writer,
    var imgUrl: String = "",
    var content: String = "",
    var feedLikesCount: Int = 0,
    var scrapCount: Int = 0
)