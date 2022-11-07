package com.d205.data.api

import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @POST("/user/kakao/join")
    suspend fun joinKakaoUser(user: UserDto): Response<String>

    @POST("/user/naver/join")
    suspend fun joinNaverUser(@Body user: UserDto): Response<String>

    @GET("/user/check/{nickname}")
    suspend fun checkNickname(nickname: String): ApiResponse<String>

    @POST("user/kakao/login")
    fun loginKakaoUser(@Body token: String): Response<UserResponse>

    @POST("user/naver/login")
    suspend fun loginNaverUser(@Body token: String): Response<UserResponse>

    @POST("user/reg")
    suspend fun updateProfile(@Body user: UserDto): Response<UserEntity>
}