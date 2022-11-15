package com.d205.data.repository.feed.remote

import android.annotation.SuppressLint
import android.util.Log
import com.d205.data.api.FeedApi
import com.d205.data.model.mypage.FeedResponse
import com.d205.data.model.mypage.MyFeedResponse
import com.d205.domain.model.common.PagingResult
import com.d205.domain.model.mypage.Feed
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

private const val TAG = "FeedRemoteDataSourceImpl"

class FeedRemoteDataSourceImpl @Inject constructor(
    private val feedApi: FeedApi
): FeedRemoteDataSource {
    @SuppressLint("LongLogTag")
    override suspend fun getUserFeeds(
        page: Int,
        pageSize: Int
    ): Flow<PagingResult<MyFeedResponse>> = flow {
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