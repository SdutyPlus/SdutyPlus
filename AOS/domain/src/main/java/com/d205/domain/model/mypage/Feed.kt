package com.d205.domain.model.mypage

data class Feed(
    val seq: Int,
    var writerSeq: Int,
    var imgUrl: String,
    var content: String
)