package com.d108.sduty.model.api

import com.d108.sduty.model.dto.Follow
import com.d108.sduty.model.dto.Profile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {
    @GET("/profile/check/{nickname}")
    suspend fun getUsedId(@Path("nickname") nickname: String): Response<Void>

    @Multipart
    @POST("/profile")
    suspend fun insertProfile(@Part imageFile: MultipartBody.Part, @Part("profile") profile: RequestBody) : Response<Profile>

    @GET("/profile/{userSeq}")
    suspend fun getProfileValue(@Path("userSeq")userSeq: Int): Response<Profile>

    @POST("/profile/follow")
    suspend fun doFollow(@Body follow: Follow): Response<Void>

    @GET("/profile/follower/{userSeq}")
    suspend fun getFollower(@Path("userSeq") userSeq: Int): Response<MutableList<Follow>>

    @GET("/profile/followee/{userSeq}")
    suspend fun getFollowee(@Path("userSeq") userSeq: Int): Response<MutableList<Follow>>

    @GET("/profile/chart/{userSeq}")
    suspend fun getGrass(@Path("userSeq") userSeq: Int): Response<List<Boolean>>

    @PUT("/profile")
    suspend fun updateProfile(@Body profile: Profile): Response<Profile>

    @Multipart
    @PUT("/profile/image")
    suspend fun updateProfileImage(@Part imageFile: MultipartBody.Part, @Part("profile") profile: RequestBody): Response<Profile>

    @PUT("/profile/timer/{userSeq}/{flag}")
    suspend fun updateIsStudying(@Path("userSeq") userSeq: Int, @Path("flag") flag: Int): Response<Void>
}