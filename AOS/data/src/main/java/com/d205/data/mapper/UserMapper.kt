package com.d205.data.mapper

import com.d205.data.model.user.UserEntity
import com.d205.domain.model.user.User

fun mapperToUser(UserEntity: UserEntity): User =
    User(
        seq = UserEntity.seq,
        id = UserEntity.email,
        nickname = UserEntity.nickname,
        imgUrl = UserEntity.imgUrl,
        job = UserEntity.job
    )