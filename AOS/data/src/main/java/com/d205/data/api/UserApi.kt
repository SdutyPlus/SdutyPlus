package com.d205.data.api

import com.d205.data.model.user.UserEntity
import com.d205.domain.model.user.UserDto
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {

    @POST("/user/kakao/join")
    suspend fun addKakaoUser(user: UserDto): ApiResponse<String>

    @POST("/user/naver/join")
    suspend fun addNaverUser(user: UserDto): ApiResponse<String>

    @GET("/user/check/{nickname}")
    suspend fun checkNickname(nickname: String): ApiResponse<String>

    @POST("/user/kakao")
    suspend fun loginKakaoUser(token: String): ApiResponse<UserEntity>

    @POST("/user/naver")
    suspend fun loginNaverUser(token: String): ApiResponse<UserEntity>
}