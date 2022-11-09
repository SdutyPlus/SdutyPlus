package com.d205.domain.model.user

data class User(
    var seq: Int = 0,
    var id: String? = "",
    var nickname: String? = "",
    var imgUrl: String? = "",
    var fcmToken: String? = "",
    var userJob: Int? = 0
)