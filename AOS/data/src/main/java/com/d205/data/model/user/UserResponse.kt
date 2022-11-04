package com.d205.data.model.user

import com.google.gson.annotations.SerializedName

data class UserResponse(val id: Int)

data class UserResponse1(val id: Int)

data class JwtDto(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
    )