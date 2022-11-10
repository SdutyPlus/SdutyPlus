package com.d205.data.mapper

import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.User

fun mapperUserResponseToUser(UserResponse: UserResponse): User =
    User(
        seq = UserResponse.userSeq,
        id = UserResponse.email,
        nickname = UserResponse.nickname,
        imgUrl = UserResponse.imgUrl,
        userJob = UserResponse.userJob,
        fcmToken = UserResponse.fcmToken,
        continuous = UserResponse.continuous
    )

fun mapperUserEntityToUser(userEntity: UserEntity): User =
    User(
        seq = userEntity.seq,
        id = userEntity.email,
        nickname = userEntity.nickname,
        imgUrl = userEntity.imgUrl,
        userJob = userEntity.userJob,
        fcmToken = userEntity.fcmToken
    )