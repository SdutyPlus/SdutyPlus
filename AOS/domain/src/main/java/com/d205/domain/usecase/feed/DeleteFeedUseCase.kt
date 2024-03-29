package com.d205.domain.usecase.feed

import android.util.Log
import com.d205.domain.repository.paging.FeedRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val TAG = "DeleteFeedUseCase"

class DeleteFeedUseCase @Inject constructor(private val feedRepository: FeedRepository){
    suspend operator fun invoke(feedSeq: Int) = feedRepository.deleteFeed(feedSeq)
}