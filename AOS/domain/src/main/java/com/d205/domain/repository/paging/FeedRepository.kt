package com.d205.domain.repository.paging

import androidx.paging.PagingSource
import com.d205.domain.model.mypage.Story
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

private const val TAG ="StoryRepository"
interface StoryRepository {
    suspend fun getUserStoryList(writerSeq: Int, userSeq: Int, page: Int, pageSize: Int): Flow<ResultState<PagingSource.LoadResult<Int, Story>>>
}