package com.d205.domain.model.feed

import android.os.Parcelable

data class HomeFeed(
    val seq: Int = 0,
    val writer: Writer ,
    val imgUrl: String
)