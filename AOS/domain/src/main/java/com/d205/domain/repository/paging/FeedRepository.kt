package com.d205.domain.repository.paging

import androidx.paging.PagingSource
import com.d205.domain.model.mypage.Feed
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

private const val TAG ="StoryRepository"
interface FeedRepository {
    suspend fun getUserFeeds(page: Int, pageSize: Int): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>>
}