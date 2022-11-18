package com.d205.domain.usecase.feed

import com.d205.domain.repository.paging.FeedRepository
import javax.inject.Inject

class DeleteScrapFeedUseCase @Inject constructor(private val feedRepository: FeedRepository) {
    suspend operator fun invoke(feedSeq: Int) = feedRepository.deleteFeed(feedSeq)
}