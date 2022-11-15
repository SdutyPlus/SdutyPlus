package com.d205.domain.model.mypage

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Feed(
    val seq: Int,
    var writerSeq: Int,
    var imgUrl: String,
    var content: String
) : Parcelable