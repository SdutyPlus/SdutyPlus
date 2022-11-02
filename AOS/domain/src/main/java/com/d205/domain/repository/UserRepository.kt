package com.d205.domain.repository

import com.d205.domain.model.user.User
import com.d205.domain.model.user.UserDto

interface UserRepository {
    suspend fun addKakaoUser(user: UserDto): Boolean
    suspend fun addNaverUser(user: UserDto): Boolean
    suspend fun checkNickname(nickname: String): Boolean
    suspend fun loginKakaoUser(token: String): User
    suspend fun loginNaverUser(token: String): User
}