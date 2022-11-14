package com.d205.data.repository.user.remote

import com.d205.data.model.BaseResponse
import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.UserDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRemoteDataSource {
    fun joinUser(user: UserDto): Flow<UserResponse>
    fun checkNickname(nickname: String): Flow<Boolean>
    fun loginKakaoUser(token: String): Flow<UserResponse>
    fun loginNaverUser(token: String): Flow<UserResponse>
    fun getUser(): Flow<UserResponse>
    fun deleteUser(): Flow<Boolean>
}