package com.d205.sdutyplus.view.story

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d205.domain.model.mypage.Story
import com.d205.domain.usecase.story.GetFeedUseCase
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.uitls.ALL_STORY

private const val TAG ="StoryDataSource"
class StoryDataSource(val flag: Int, private val useCase: GetFeedUseCase, private val userSeq: Int, private val writerSeq: Int): PagingSource<Int, Story>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key?: 0
            var response: LoadResult<Int, Story> = LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )

            when(flag){
                ALL_STORY -> useCase.invoke(writerSeq, userSeq, page, 18).collect {
                    if(it is ResultState.Success) {
                        Log.d(TAG, "GetStoryUseCase collect : ${it.data}")
                        response = it.data
                    }
                }
                //SCRAP_STORY -> response = storyApi.getScrapList(userSeq, page, 18)
            }
            response
        } catch (e: Exception) {
            Log.d(TAG, "load: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        TODO("Not yet implemented")
    }
}