package com.d205.data.repository.user.remote

import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRemoteDataSource {
    fun joinKakaoUser(user: UserDto): Flow<Response<UserEntity>>
    fun joinNaverUser(user: UserDto): Flow<Response<UserEntity>>
    suspend fun checkNickname(nickname: String): ApiResponse<String>
    fun loginKakaoUser(token: String): Flow<Response<UserResponse>>
    fun loginNaverUser(token: String): Flow<Response<UserResponse>>
}