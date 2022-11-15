package com.d205.data.repository.feed.remote


import com.d205.data.model.feed.HomeFeedResponse
import com.d205.data.model.mypage.MyFeedResponse
import com.d205.domain.model.common.PagingResult
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface FeedRemoteDataSource {
    suspend fun getUserFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<MyFeedResponse>>

    suspend fun getHomeFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<HomeFeedResponse>>

    suspend fun createFeed(
        imageFile: MultipartBody.Part,
        content: MultipartBody.Part
    ): Flow<Boolean>
}