package com.d205.data.model.user

data class UserEntity(
    var seq: Int = -1,
    var id: String = "",
    var nickName: String = "",
    var fcmToken: String = "",
    var job: Int = 1
)