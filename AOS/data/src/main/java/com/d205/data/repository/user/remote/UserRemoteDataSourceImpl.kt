package com.d205.data.repository.user.remote

import com.d205.data.api.UserRestApi
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userApi: UserRestApi
): UserRemoteDataSource {

    override suspend fun addKakaoUser(user: UserDto): ApiResponse<String> {
        return userApi.addKakaoUser(user)
    }
}