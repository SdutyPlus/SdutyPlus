package com.d205.domain.model.user

import java.util.*

data class User(
    var seq: Int = -1,
    var id: String,
    var pass: String,
    var nickName: String,
    var fcmToken: String = "",
    var regtime: Date? = null,
    var userPublic: Int = 1
)