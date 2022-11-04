package com.d205.domain.repository

import com.d205.domain.model.user.User
import com.d205.domain.model.user.UserDto
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun addKakaoUser(user: UserDto): Boolean
    suspend fun addNaverUser(user: UserDto): Boolean
    suspend fun checkNickname(nickname: String): Boolean
    fun loginKakaoUser(token: String): User
    fun loginNaverUser(token: String): Flow<ResultState<User>>
}