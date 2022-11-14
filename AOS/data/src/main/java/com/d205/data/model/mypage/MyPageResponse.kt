package com.d205.data.model.mypage


data class MyFeedResponse(
    val seq: Int = 0,
    var writerSeq: Int = 0,
    var imgUrl: String = "",
    var content: String = ""
)