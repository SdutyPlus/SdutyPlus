package com.d205.domain.repository

import com.d205.domain.model.user.User
import com.d205.domain.model.user.UserDto
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun joinKakaoUser(user: UserDto): Flow<ResultState<User>>
    fun joinNaverUser(user: UserDto): Flow<ResultState<User>>
    suspend fun checkNickname(nickname: String): Boolean
    fun loginKakaoUser(token: String): Flow<ResultState<User>>
    fun loginNaverUser(token: String): Flow<ResultState<User>>
}