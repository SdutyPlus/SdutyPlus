package com.d205.domain.model.mypage

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feed(
    val seq: Int,
    var writerSeq: Long,
    var nickname: String,
    var profileImgUrl: String? = null,
    var feedImgUrl: String,
    var content: String,
    var feedLikesCount: Int = 0,
    var scrapCount: Int = 0
) : Parcelable