package com.d205.data.model.mypage

data class FeedResponse(
    val seq: Int,
    var writerSeq: Int,
    var imgUrl: String,
    var thumbnail: String?,
    var content: String?
)