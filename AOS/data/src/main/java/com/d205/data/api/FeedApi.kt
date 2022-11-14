package com.d205.data.api

import com.d205.data.model.BaseResponse
import com.d205.data.model.mypage.FeedResponse
import com.d205.domain.model.common.PagingResult
import com.d205.domain.model.mypage.Feed
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface FeedApi {
    @GET("story/writer/{writerSeq}/{userSeq}")
    suspend fun getUserStoryList(
        @Query("page")page: Int,
        @Query("size")pageSize: Int): BaseResponse<PagingResult<FeedResponse>>

    @Multipart
    @POST("feed")
    suspend fun createFeed(@Part imageFile: MultipartBody.Part, @Part content: MultipartBody.Part): BaseResponse<Boolean>

}