package com.d205.domain.model.mypage

data class Feed(
    val seq: Int,
    var writerSeq: Int,
    var imgUrl: String,
    var thumbnail: String,
    var jobHashtag: Int?,
    var contents: String,
    var regtime: String?,
    var feedPublic: Int,
    var feedWarning: Int
)