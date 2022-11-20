package com.d205.domain.model.feed

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feed(
    val seq: Int,
    var writerSeq: Long,
    var nickname: String,
    val userJob: String,
    var profileImgUrl: String? = null,
    var feedImgUrl: String,
    var content: String,
    var feedLikesCount: Int = 0,
    var scrapCount: Int = 0,
    var feedScrapFlag: Boolean = false
) : Parcelable