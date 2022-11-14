package com.d205.data.repository.feed.remote

import com.d205.data.model.mypage.FeedResponse
import com.d205.domain.model.common.PagingResult
import com.d205.domain.model.mypage.Feed
import kotlinx.coroutines.flow.Flow

interface FeedRemoteDataSource {
    suspend fun getUserFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<FeedResponse>>
}