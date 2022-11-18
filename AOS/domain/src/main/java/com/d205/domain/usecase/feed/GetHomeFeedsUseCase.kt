package com.d205.domain.usecase.feed

import androidx.paging.PagingSource
import com.d205.domain.model.feed.Feed
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.repository.paging.FeedRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHomeFeedsUseCase @Inject constructor(private val storyPagingRepository: FeedRepository) {
    suspend operator fun invoke(page: Int, pageSize: Int): Flow<ResultState<PagingSource.LoadResult<Int, Feed>>> =
        storyPagingRepository.getHomeFeeds(page, pageSize)

}