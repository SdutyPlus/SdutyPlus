package com.d205.data.model.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    var seq: Int = 0,
    var email: String? = "",
    var nickname: String? = "",
    var imgUrl: String? = "",
    var fcmToken: String? = "",
    @SerializedName("job")
    var userJob: Int = 0,
    val jwtDto: JwtDto? = null
)

data class UserEntity(
    var seq: Int = 0,
    var email: String? = "",
    var nickname: String? = "",
    var imgUrl: String? = "",
    var fcmToken: String? = "",
    @SerializedName("job")
    var userJob: Int = 0
)

data class JwtDto(
    var accessToken: String? = "",
    var refreshToken: String? = ""
)