package com.d205.data.repository.feed.remote

import com.d205.data.api.FeedApi
import com.d205.domain.model.common.PagingResult
import com.d205.domain.model.mypage.Story
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FeedRemoteDataSourceImpl @Inject constructor(
    private val feedApi: FeedApi
): FeedRemoteDataSource {
    override suspend fun getUserStoryList(
        writerSeq: Int,
        userSeq: Int,
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<Story>> = flow {
        feedApi.getUserStoryList(writerSeq, userSeq, page, pageSize)
    }

}