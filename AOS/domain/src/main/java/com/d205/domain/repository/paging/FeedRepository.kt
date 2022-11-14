package com.d205.domain.repository.paging

import android.graphics.Bitmap
import androidx.paging.PagingSource
import com.d205.domain.model.mypage.Feed
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

private const val TAG ="FeedRepository"
interface FeedRepository {
    suspend fun getUserFeeds(page: Int, pageSize: Int): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>>

    suspend fun createFeed(
        feedImageBitmap: Bitmap,
        content: String
    ): Flow<ResultState<Boolean>>
}