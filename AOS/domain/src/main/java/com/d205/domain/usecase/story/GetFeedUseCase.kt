package com.d205.domain.usecase.story

import androidx.paging.PagingSource
import com.d205.domain.model.mypage.Story
import com.d205.domain.repository.paging.StoryRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(private val storyPagingRepository: StoryRepository) {
    suspend operator fun invoke(writerSeq: Int, userSeq: Int, page: Int, pageSize: Int): Flow<ResultState<PagingSource.LoadResult<Int, Story>>> =
        storyPagingRepository.getUserStoryList(writerSeq, userSeq, page, pageSize)
}