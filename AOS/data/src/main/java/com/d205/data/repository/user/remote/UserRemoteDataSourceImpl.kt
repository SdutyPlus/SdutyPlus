package com.d205.data.repository.user.remote

import com.d205.data.api.UserApi
import com.d205.data.model.user.UserEntity
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userApi: UserApi
): UserRemoteDataSource {

    override suspend fun addKakaoUser(user: UserDto): ApiResponse<String> =
        userApi.addKakaoUser(user)

    override suspend fun addNaverUser(user: UserDto): ApiResponse<String> =
        userApi.addNaverUser(user)

    override suspend fun checkNickname(nickname: String): ApiResponse<String> =
        userApi.checkNickname(nickname)

    override suspend fun loginKakaoUser(token: String): ApiResponse<UserEntity> =
        userApi.loginKakaoUser(token)

    override suspend fun loginNaverUser(token: String): ApiResponse<UserEntity> =
        userApi.loginNaverUser(token)
}