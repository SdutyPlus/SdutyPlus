package com.d108.sduty.model.api

import com.d108.sduty.model.dto.AuthInfo
import com.d108.sduty.model.dto.User
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @POST("/user")
    suspend fun login(@Body user: User): Response<User>

    @GET("/user/join/{id}")
    suspend fun getUsedId(@Path("id")id: String): Response<Void>

    @POST("/user/join")
    suspend fun join(@Body user: User): Response<Void>

    @GET("/user/test")
    suspend fun getTest(): Response<List<String>>

    @POST("/user/kakao")
    suspend fun kakaoLogin(@Body token: String): Response<User>

    @POST("/user/naver")
    suspend fun naverLogin(@Body token: String): Response<User>

    @POST("/user/kakao/join")
    suspend fun kakaoUserInfo(@Body token: String): Response<User>

    @POST("/user/naver/join")
    suspend fun naverUserInfo(@Body token: String): Response<User>

    @POST("/user/auth")
    suspend fun sendAuthInfo(@Body authInfo: AuthInfo): Response<Void>

    @POST("/user/auth/check")
    suspend fun checkAuthCode(@Body authInfo: AuthInfo): Response<Void>

    @GET("/user/id/{name}/{tel}")
    suspend fun findId(@Path("name") name: String, @Path("tel") tel: String): Response<String>

    @PUT("/user/pwd")
    suspend fun changePw(@Body user: User): Response<Void>

    @DELETE("/user/{seq}")
    suspend fun deleteUser(@Path("seq")userSeq: Int): Response<Void>

    @GET("/user/{user_seq}")
    suspend fun getUserValue(@Path("user_seq")userSeq: Int): Response<User>

    @PUT("/user")
    suspend fun updateUser(@Body user: User): Response<Void>

    @GET("/user/info/{id}")
    suspend fun getUserInfo(@Path("id") id: String): Response<User>

}