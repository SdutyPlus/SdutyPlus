package com.d205.data.api

import com.d205.data.model.BaseResponse
import com.d205.data.model.user.UserEntity
import com.d205.data.model.user.UserResponse
import com.d205.domain.model.user.UserDto
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    // 닉네임 중복 체크
    @GET("user/check/{nickname}")
    suspend fun checkNickname(@Path("nickname") nickname: String): BaseResponse<Boolean>

    // 카카오 로그인
    @POST("user/kakao/login")
    suspend fun loginKakaoUser(@Body token: String): BaseResponse<UserResponse>

    // 네이버 로그인
    @POST("user/naver/login")
    suspend fun loginNaverUser(@Body token: String): BaseResponse<UserResponse>

    // 프로필 업데이트
    @POST("user/reg")
    suspend fun updateProfile(@Body user: UserDto): BaseResponse<UserResponse>

    // 유저 정보 조회
    @GET("user")
    suspend fun getUser(): BaseResponse<UserResponse>

    // 유저 정보 삭제
    @DELETE("user")
    suspend fun deleteUser(): BaseResponse<Boolean>
}