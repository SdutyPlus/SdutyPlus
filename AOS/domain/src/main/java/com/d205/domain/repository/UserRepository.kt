package com.d205.domain.repository

import com.d205.domain.model.user.UserDto

interface UserRepository {
    suspend fun addKakaoUser(user: UserDto): Boolean
}