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
    override suspend fun getScrapFeeds(
        page: Int,
        pageSize: Int): Flow<PagingResult<FeedResponse>>  = flow {
        Log.d(TAG, "getScrapFeeds page: $page")
        val response = feedApi.getScrapFeeds(page, pageSize)
        if(response.status == 200 && response.data != null) {
            emit(response.data)
        }
        else {
            emit(PagingResult(-1,-1, emptyList()))
        }
    }.catch { e ->
        Log.d(TAG, "getScrapFeeds: ${e.message}")
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

    @SuppressLint("LongLogTag")
    override suspend fun deleteFeed(feedSeq: Int): Flow<Boolean> = flow {
        Log.d(TAG, "deleteFeed: start!")
        val response = feedApi.deleteFeed(feedSeq)
        if(response.status == 200) {
            emit(true)
            Log.d(TAG, "deleteFeed: true")
        }
        else {
            emit(false)
            Log.d(TAG, "deleteFeed: false")
        }
    }.catch { e ->
        Log.d(TAG, "deleteFeed Error : $e")
    }

    @SuppressLint("LongLogTag")
    override suspend fun scrapFeed(feedSeq: Int): Flow<Boolean> = flow{
        Log.d(TAG, "scrapFeed: start!")
        val response = feedApi.scrapFeed(feedSeq)
        if(response.status == 200) {
            emit(true)
            Log.d(TAG, "scrapFeed: true")
        }
        else {
            emit(false)
            Log.d(TAG, "scrapFeed: false")
        }
    }.catch { e ->
        Log.d(TAG, "scrapFeed Error : $e")
    }

    @SuppressLint("LongLogTag")
    override suspend fun deleteScrapFeed(feedSeq: Int): Flow<Boolean> = flow {
        Log.d(TAG, "deleteScrapFeed: start!")
        val response = feedApi.deleteScrapFeed(feedSeq)
        if(response.status == 200) {
            emit(true)
            Log.d(TAG, "deleteScrapFeed: true")
        }
        else {
            emit(false)
            Log.d(TAG, "deleteScrapFeed: false")
        }
    }.catch { e ->
        Log.d(TAG, "deleteScrapFeed Error : $e")
    }
}