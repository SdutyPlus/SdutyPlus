package com.d205.data.api

import com.d205.data.model.BaseResponse
import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @GET("/user/check/{nickname}")
    suspend fun checkNickname(nickname: String): ApiResponse<String>

    @POST("user/kakao/login")
    fun loginKakaoUser(@Body token: String): Response<UserResponse>

    @POST("user/naver/login")
    suspend fun loginNaverUser(@Body token: String): BaseResponse<UserResponse>

    @POST("user/reg")
    suspend fun updateProfile(@Body user: UserDto): Response<UserEntity>
}