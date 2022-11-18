package com.d205.domain.repository.paging

import android.graphics.Bitmap
import androidx.paging.PagingSource
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.model.feed.Feed
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

private const val TAG ="FeedRepository"
interface FeedRepository {
    suspend fun getUserFeeds(page: Int, pageSize: Int): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>>

    suspend fun getHomeFeeds(page: Int, pageSize: Int): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>>

    suspend fun getScrapFeeds(page: Int, pageSize: Int): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>>

    suspend fun createFeed(
        feedImageBitmap: Bitmap,
        content: String
    ): Flow<ResultState<Boolean>>

    suspend fun deleteFeed(feedSeq: Int): Flow<ResultState<Boolean>>

    suspend fun scrapFeed(feedSeq: Int): Flow<ResultState<Boolean>>
    suspend fun deleteScrapFeed(feedSeq: Int): Flow<ResultState<Boolean>>
}