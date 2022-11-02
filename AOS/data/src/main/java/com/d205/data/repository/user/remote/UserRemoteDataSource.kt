package com.d205.data.repository.user.remote

import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response

interface UserRemoteDataSource {
    suspend fun addKakaoUser(user: UserDto): ApiResponse<String>
}