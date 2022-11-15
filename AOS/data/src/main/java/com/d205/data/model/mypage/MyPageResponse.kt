package com.d205.data.model.mypage


data class MyFeedResponse(
    val seq: Int = 0,
    var writerSeq: Int = 0,
    var nickname: String = "",
    var profileImgUrl: String = "",
    var feedImgUrl: String = "",
    var content: String = "",
    var scrapCnt: Int = 0
)