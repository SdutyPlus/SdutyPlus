package com.d205.data.model.mypage

data class FeedResponse(
    val seq: Int,
    var writerSeq: Int,
    var imageSource: String,
    var thumbnail: String,
    var jobHashtag: Int?,
    var contents: String,
    var regtime: String?,
    var storyPublic: Int,
    var storyWarning: Int,
    var interestHashtag: MutableList<Int>?
)