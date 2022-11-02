package com.d205.data.mapper

import com.d205.data.model.user.UserEntity
import com.d205.domain.model.user.User

fun mapperToUser(userEntity: UserEntity): User =
    User(
        seq = userEntity.seq,
        id = userEntity.id,
        nickName = userEntity.nickName,
        job = userEntity.job
    )