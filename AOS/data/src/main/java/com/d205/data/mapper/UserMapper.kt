package com.d205.data.mapper

import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.User

fun mapperUserResponseToUser(UserResponse: UserResponse): User =
    User(
        seq = UserResponse.seq,
        id = UserResponse.email,
        nickname = UserResponse.nickname,
        imgUrl = UserResponse.imgUrl,
        job = UserResponse.job,
        fcmToken = UserResponse.fcmToken
    )

fun mapperUserEntityToUser(userEntity: UserEntity): User =
    User(
        seq = userEntity.seq,
        id = userEntity.email,
        nickname = userEntity.nickname,
        imgUrl = userEntity.imgUrl,
        job = userEntity.job,
        fcmToken = userEntity.fcmToken
    )