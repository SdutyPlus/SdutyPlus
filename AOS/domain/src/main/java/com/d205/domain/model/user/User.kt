package com.d205.domain.model.user

import java.util.*

data class User(
    var seq: Int = -1,
    var id: String,
    var nickName: String,
    var fcmToken: String = "",
    var job: Int = 1
)