package com.d205.data.model.user

import com.google.gson.annotations.SerializedName

data class UserResponse(
    var userSeq: Int = 0,
    var email: String? = "",
    var nickname: String? = "",
    var imgUrl: String? = "",
    var fcmToken: String? = "",
    @SerializedName("job")
    var userJob: String = "",
    val jwtDto: JwtDto? = null,
    val continuous: Int = 0
)

data class UserEntity(
    var seq: Int = 0,
    var email: String? = "",
    var nickname: String? = "",
    var imgUrl: String? = "",
    var fcmToken: String? = "",
    @SerializedName("job")
    var userJob: String = ""
)

data class JwtDto(
    var accessToken: String? = "",
    var refreshToken: String? = ""
)