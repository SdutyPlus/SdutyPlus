package com.d205.data.repository.feed

import android.graphics.Bitmap
import android.util.Log
import androidx.paging.PagingSource
import com.d205.data.mapper.mapperFeedResponseToHomeFeeds
import com.d205.data.mapper.mapperFeedResponseToFeed
import com.d205.data.repository.feed.local.FeedLocalDataSource
import com.d205.data.repository.feed.remote.FeedRemoteDataSource
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.model.feed.Feed
import com.d205.domain.repository.paging.FeedRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
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
        Log.d(TAG, "getUserFeeds: Loading")
        emit(ResultState.Loading)

        feedRemoteDataSource.getUserFeeds(page, pageSize).collect { it ->

            Log.d(TAG, "getUserFeeds collect : ${it.result}")
            if(it.result.isNotEmpty()) {
                Log.d(TAG, "getUserFeeds: not empty")
                emit(ResultState.Success(PagingSource.LoadResult.Page(
                    data = it.result.map { feedResponse ->
                        mapperFeedResponseToFeed(feedResponse)
                    },
                    prevKey = if(page == 0) null else page - 1,
                    nextKey = if(page == it.totalPage) null else page + 1
                )))
            } else {
                emit(ResultState.Success(PagingSource.LoadResult.Page(
                    data = emptyList<Feed>(),
                    prevKey = if(page == 0) null else page - 1,
                    nextKey = null
                )))
            }
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override suspend fun getHomeFeeds(
        page: Int,
        pageSize: Int
    ): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>> = flow {
        Log.d(TAG, "getHomeFeeds: Loading")
        emit(ResultState.Loading)

        feedRemoteDataSource.getHomeFeeds(page, pageSize).collect { it ->

            Log.d(TAG, "getHomeFeeds collect : ${it.result}")
            if(it.result.isNotEmpty()) {
                Log.d(TAG, "getHomeFeeds: not empty")
                emit(ResultState.Success(PagingSource.LoadResult.Page(
                    data = it.result.map { homeFeedResponse ->
                        mapperFeedResponseToFeed(homeFeedResponse)// todo
                    },
                    prevKey = if(page == 0) null else page - 1,
                    nextKey = if(page == it.totalPage) null else page + 1
                )))
            } else {
                emit(ResultState.Success(PagingSource.LoadResult.Page(
                    data = emptyList<Feed>(),
                    prevKey = if(page == 0) null else page - 1,
                    nextKey = null
                )))
            }

        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override suspend fun getScrapFeeds(
        page: Int,
        pageSize: Int
    ): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>>  = flow {
        Log.d(TAG, "getScrapFeeds: Loading")
        emit(ResultState.Loading)

        feedRemoteDataSource.getScrapFeeds(page, pageSize).collect { it ->
            Log.d(TAG, "getScrapFeeds collect : ${it.result}")
            if(it.result.isNotEmpty()) {
                Log.d(TAG, "getScrapFeeds: not empty")
                emit(ResultState.Success(PagingSource.LoadResult.Page(
                    data = it.result.map { feedResponse ->
                        mapperFeedResponseToFeed(feedResponse)
                    },
                    prevKey = if(page == 0) null else page - 1,
                    nextKey = if(page == it.totalPage) null else page + 1
                )))
            } else {
                emit(ResultState.Success(PagingSource.LoadResult.Page(
                    data = emptyList<Feed>(),
                    prevKey = if(page == 0) null else page - 1,
                    nextKey = null
                )))
            }
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }


    override suspend fun createFeed(
        feedImageBitmap: Bitmap,
        content: String
    ): Flow<ResultState<Boolean>> = flow<ResultState<Boolean>> {
        Log.d(TAG, "createFeed: Loading")
        emit(ResultState.Loading)

        val bitmapRequestBody = feedImageBitmap?.let {
            BitmapRequestBody(it)
        }
        val fileName = "feed/" + System.currentTimeMillis().toString()+".png"
        val imageFileBody : MultipartBody.Part = MultipartBody.Part.createFormData("img", fileName, bitmapRequestBody!!)

//        val json = Gson().toJson(content)
//        val feedBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
//        val contentResultBody = RequestBody.create(MediaType.parse("text/plain"), content)
        val contentBody = MultipartBody.Part.createFormData("content", content.toString())

        feedRemoteDataSource.createFeed(imageFileBody, contentBody).collect{
            Log.d(TAG, "createFeed: collect  success : $it")
            emit(ResultState.Success(it))
        }

    }.catch { e ->
        emit(ResultState.Error(e))
        Log.d(TAG, "createFeed success fail: $e")
    }

    override suspend fun deleteFeed(feedSeq: Int): Flow<ResultState<Boolean>> = flow {
        Log.d(TAG, "deleteFeed: Loading")
        //emit(ResultState.Loading)

        feedRemoteDataSource.deleteFeed(feedSeq).collect {
            Log.d(TAG, "deleteFeed collect : Success!")
            emit(ResultState.Success(it))
        }
        Log.d(TAG, "deleteFeed collect : Finished!")
    }.catch { e->
        //emit(ResultState.Error(e))
        Log.d(TAG, "deleteFeed Error")
    }

    override suspend fun scrapFeed(feedSeq: Int): Flow<ResultState<Boolean>> = flow {
        Log.d(TAG, "scrapFeed: Loading")

        feedRemoteDataSource.scrapFeed(feedSeq).collect {
            Log.d(TAG, "scrapFeed collect : Success!")
            emit(ResultState.Success(it))
        }
        Log.d(TAG, "scrapFeed collect : Finished!")
    }.catch { e->
        //emit(ResultState.Error(e))
        Log.d(TAG, "scrapFeed Error")
    }

    override suspend fun deleteScrapFeed(feedSeq: Int): Flow<ResultState<Boolean>> = flow {
        Log.d(TAG, "deleteScrapFeed: Loading")

        feedRemoteDataSource.deleteScrapFeed(feedSeq).collect {
            Log.d(TAG, "deleteScrapFeed collect : Success!")
            emit(ResultState.Success(it))
        }
        Log.d(TAG, "deleteScrapFeed collect : Finished!")
    }.catch { e ->
        //emit(ResultState.Error(e))
        Log.d(TAG, "deleteScrapFeed Error")
    }

    override suspend fun reportFeed(feedSeq: Int): Flow<ResultState<Boolean>> = flow {
        Log.d(TAG, "reportFeed: Loading")

        feedRemoteDataSource.reportFeed(feedSeq).collect {
            Log.d(TAG, "reportFeed collect : Success!")
            emit(ResultState.Success(it))
        }
        Log.d(TAG, "reportFeed collect : Finished!")
    }.catch { e ->
        Log.d(TAG, "reportFeed Error")
    }

    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = MediaType.parse("image/*")!!
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }
}