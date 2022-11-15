package com.d205.data.repository.feed.remote


import com.d205.data.model.feed.FeedResponse
import com.d205.domain.model.common.PagingResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface FeedRemoteDataSource {
    suspend fun getUserFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<FeedResponse>>

    suspend fun getHomeFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<FeedResponse>>

    suspend fun createFeed(
        imageFile: MultipartBody.Part,
        content: MultipartBody.Part
    ): Flow<Boolean>
}