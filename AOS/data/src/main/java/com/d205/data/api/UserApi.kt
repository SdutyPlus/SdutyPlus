package com.d205.data.api

import com.d205.data.model.user.UserEntity
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {

    @POST("/user/kakao/join")
    suspend fun addKakaoUser(user: UserDto): ApiResponse<String>

    @POST("/user/naver/join")
    suspend fun addNaverUser(@Body user: UserDto): ApiResponse<String>

    @GET("/user/check/{nickname}")
    suspend fun checkNickname(nickname: String): ApiResponse<String>

    @POST("user/kakao/login")
    fun loginKakaoUser(@Body token: String): Response<UserEntity>

    @POST("user/naver/login")
    suspend fun loginNaverUser(@Body token: String): Response<UserEntity>

    @POST("user/reg")
    suspend fun updateProfile(@Body profile: Map<String, Any>): Response<UserEntity>
}