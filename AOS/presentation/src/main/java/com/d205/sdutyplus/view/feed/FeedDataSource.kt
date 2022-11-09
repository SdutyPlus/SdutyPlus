package com.d205.sdutyplus.view.feed

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d205.domain.model.mypage.Feed
import com.d205.domain.usecase.feed.GetFeedsUseCase
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.uitls.ALL_STORY

private const val TAG ="StoryDataSource"
class FeedDataSource(val flag: Int, private val getFeedsUseCase: GetFeedsUseCase): PagingSource<Int, Feed>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        return try {
            val page = params.key?: 0
            var response: LoadResult<Int, Feed> = LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )

            when(flag){
                ALL_STORY -> getFeedsUseCase.invoke(page, 18).collect {
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

    override fun getRefreshKey(state: PagingState<Int, Feed>): Int? {
        TODO("Not yet implemented")
    }
}