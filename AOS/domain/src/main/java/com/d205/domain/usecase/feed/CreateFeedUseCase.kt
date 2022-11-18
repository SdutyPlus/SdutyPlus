package com.d205.domain.usecase.feed

import android.graphics.Bitmap
import android.util.Log
import com.d205.domain.repository.paging.FeedRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

private const val TAG = "CreateFeedUseCase"

class CreateFeedUseCase @Inject constructor(private val feedRepository: FeedRepository) {
    operator suspend fun invoke(feedImageBitmap: Bitmap, content: String): Flow<ResultState<Boolean>> = flow {
        feedRepository.createFeed(feedImageBitmap, content).collect {
            if (it is ResultState.Success) {
                Log.d(TAG, "invoke: ResultState.Success")
                emit(it as ResultState.Success)
            }
            else if (it is ResultState.Loading) {
                Log.d(TAG, "invoke: ResultState.Loading")
                emit(it as ResultState.Loading)
            }
            else {
                Log.d(TAG, "invoke: ResultState.Error")
                emit(it as ResultState.Error)
            }
        }
    }
}