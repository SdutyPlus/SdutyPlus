package com.d205.domain.repository

import com.d205.domain.model.user.User
import com.d205.domain.model.user.UserDto
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun joinUser(user: UserDto): Flow<ResultState<User>>
    fun checkNickname(nickname: String): Flow<ResultState<Boolean>>
    fun loginKakaoUser(token: String): Flow<ResultState<User>>
    fun loginNaverUser(token: String): Flow<ResultState<User>>
    fun getUser(): Flow<ResultState<User>>
    fun deleteUser(): Flow<ResultState<Boolean>>
    fun updateUser(user: UserDto, prevProfileImageUrl: String?): Flow<ResultState<User>>
    fun checkJwt(): Flow<ResultState<Boolean>>
}