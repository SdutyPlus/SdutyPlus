package com.d205.data.repository.feed.remote

import android.annotation.SuppressLint
import android.util.Log
import com.d205.data.api.FeedApi
import com.d205.data.model.feed.FeedResponse
import com.d205.domain.model.common.PagingResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

private const val TAG = "FeedRemoteDataSourceImpl"

class FeedRemoteDataSourceImpl @Inject constructor(
    private val feedApi: FeedApi
): FeedRemoteDataSource {

    @SuppressLint("LongLogTag")
    override suspend fun getUserFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<FeedResponse>> = flow {
        Log.d(TAG, "getUserFeeds page: $page")
        val response = feedApi.getUserStoryList(page, pageSize)
        if(response.status == 200 && response.data != null) {
            emit(response.data)
        }
        else {
            emit(PagingResult(-1,-1, emptyList()))
        }
    }.catch { e ->
        Log.d(TAG, "getUserFeeds: ${e.message}")
    }

    @SuppressLint("LongLogTag")
    override suspend fun getHomeFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<FeedResponse>> = flow {
        Log.d(TAG, "getHomeFeeds page: $page")
        val response = feedApi.getHomeFeeds(page, pageSize)
        if(response.status == 200 && response.data != null) {
            emit(response.data)
        }
        else {
            emit(PagingResult(-1,-1, emptyList()))
        }
    }.catch { e ->
        Log.d(TAG, "getHomeFeeds: ${e.message}")
    }

    @SuppressLint("LongLogTag")
    override suspend fun createFeed(
        imageFile: MultipartBody.Part,
        content: MultipartBody.Part
    ): Flow<Boolean> = flow {
        Log.d(TAG, "createFeed: $imageFile & $content")
        val response = feedApi.createFeed(imageFile, content)
        if(response.status == 200) {
            emit(true)
            Log.d(TAG, "createFeed: true")
        }
        else {
            emit(false)
            Log.d(TAG, "createFeed: false")
        }
    }.catch { e ->
        Log.d(TAG, "CreateFeedDataSourceError: $e")
    }
}