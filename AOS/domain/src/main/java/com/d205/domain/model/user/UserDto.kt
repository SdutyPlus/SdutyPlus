package com.d205.domain.model.user

import com.google.gson.annotations.SerializedName

data class UserDto (
    var imgUrl: String,
    var nickname: String,
    var userJob: Int
)