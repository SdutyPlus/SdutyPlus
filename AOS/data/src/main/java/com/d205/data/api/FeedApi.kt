package com.d205.data.api

import com.d205.data.model.BaseResponse
import com.d205.data.model.mypage.FeedResponse
import com.d205.domain.model.common.PagingResult
import com.d205.domain.model.mypage.Feed
import retrofit2.http.GET
import retrofit2.http.Query

interface FeedApi {
    @GET("story/writer/{writerSeq}/{userSeq}")
    suspend fun getUserStoryList(
        @Query("page")page: Int,
        @Query("size")pageSize: Int): BaseResponse<PagingResult<FeedResponse>>

}