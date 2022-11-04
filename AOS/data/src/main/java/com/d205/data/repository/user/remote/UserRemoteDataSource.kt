package com.d205.data.repository.user.remote

import com.d205.data.model.user.UserEntity
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface UserRemoteDataSource {
    suspend fun addKakaoUser(user: UserDto): ApiResponse<String>
    suspend fun addNaverUser(user: UserDto): ApiResponse<String>
    suspend fun checkNickname(nickname: String): ApiResponse<String>
    fun loginKakaoUser(token: String): Response<UserEntity>
    fun loginNaverUser(token: String): Flow<Response<UserEntity>>
}