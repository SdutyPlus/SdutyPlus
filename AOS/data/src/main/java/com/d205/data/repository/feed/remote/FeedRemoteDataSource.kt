package com.d205.data.repository.feed.remote


import com.d205.data.model.mypage.MyFeedResponse
import com.d205.domain.model.common.PagingResult
import kotlinx.coroutines.flow.Flow

interface FeedRemoteDataSource {
    suspend fun getUserFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<MyFeedResponse>>
}