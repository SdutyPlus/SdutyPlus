package com.d205.data.api

import com.d205.data.model.BaseResponse
import com.d205.domain.model.common.PagingResult
import com.d205.domain.model.mypage.Story
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FeedApi {
    @GET("story/writer/{writerSeq}/{userSeq}")
    suspend fun getUserStoryList(
        @Path("writerSeq")writerSeq: Int,
        @Path("userSeq")userSeq: Int,
        @Query("page")page: Int,
        @Query("size")pageSize: Int): BaseResponse<PagingResult<Story>>

}