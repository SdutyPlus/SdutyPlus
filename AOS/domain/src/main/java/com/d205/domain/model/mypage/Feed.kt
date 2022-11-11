package com.d205.domain.model.mypage

data class Feed(
    val seq: Int,
    var writerSeq: Int,
    var imgUrl: String,
    var thumbnail: String = "",
    var jobHashtag: Int? = 0,
    var contents: String = "",
    var regtime: String? = null,
    var feedPublic: Int = 0,
    var feedWarning: Int = 0
)