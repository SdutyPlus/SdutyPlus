package com.d205.domain.model.user

import java.util.*

data class UserDto (
    var token: String,
    var nickName: String,
    var job: Int
)