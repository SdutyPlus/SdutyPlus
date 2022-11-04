package com.d205.data.model.user

data class UserEntity(
    var seq: Int = 0,
    var email: String = "",
    var nickname: String = "",
    var imgUrl: String = "",
    var fcmToken: String = "",
    var job: Int = 0,
    val jwtDto: JwtDto? = null
)