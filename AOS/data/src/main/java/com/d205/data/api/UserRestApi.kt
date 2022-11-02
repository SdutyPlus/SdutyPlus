package com.d205.data.api

import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.POST

interface UserRestApi {

    @POST("/user/kakao/join")
    suspend fun addKakaoUser(user: UserDto): ApiResponse<String>

}