package com.d205.data.repository.feed.remote

import com.d205.domain.model.common.PagingResult
import com.d205.domain.model.mypage.Story
import kotlinx.coroutines.flow.Flow

interface FeedRemoteDataSource {
    suspend fun getUserStoryList(
        writerSeq: Int,
        userSeq: Int,
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<Story>>
}