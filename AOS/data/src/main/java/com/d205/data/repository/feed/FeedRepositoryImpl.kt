package com.d205.data.repository.feed

import android.util.Log
import androidx.paging.PagingSource
import com.d205.data.mapper.mapperFeedResponseToFeed
import com.d205.data.repository.feed.local.FeedLocalDataSource
import com.d205.data.repository.feed.remote.FeedRemoteDataSource
import com.d205.domain.model.mypage.Feed
import com.d205.domain.repository.paging.FeedRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "FeedRepositoryImpl"

class FeedRepositoryImpl @Inject constructor(
    private val feedRemoteDataSource: FeedRemoteDataSource,
    private val feedLocalDataSource: FeedLocalDataSource
): FeedRepository {

    override suspend fun getUserFeeds(
        page: Int,
        pageSize: Int
    ): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>> = flow {
        Log.d(TAG, "joinUser: Loading")
        emit(ResultState.Loading)

        feedRemoteDataSource.getUserFeeds(page, pageSize).collect { it ->
            Log.d(TAG, "getUserStoryList: $it")
            emit(ResultState.Success(PagingSource.LoadResult.Page(
                data = it.result.map { feedResponse ->
                    mapperFeedResponseToFeed(feedResponse)
                },
                prevKey = if(page == 0) null else page - 1,
                nextKey = if(page == it.totalPage) null else page + 1
            )))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }
}