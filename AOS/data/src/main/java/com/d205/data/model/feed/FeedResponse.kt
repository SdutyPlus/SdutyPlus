package com.d205.data.model.feed

import com.d205.domain.model.feed.Writer

data class HomeFeedResponse(
    val seq: Int = 0,
    val writer: Writer ,
    val imgUrl: String
)
