package com.d205.sdutyplus.view.feed

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d205.domain.model.feed.Feed
import com.d205.domain.model.feed.HomeFeed
import com.d205.domain.usecase.feed.GetHomeFeedsUseCase
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.uitls.HOME_ALL_STORY


private const val TAG ="HomeFeedDataSource"
class HomeFeedDataSource(val flag: Int, private val getHomeFeedsUseCase: GetHomeFeedsUseCase
): PagingSource<Int, Feed>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Feed> {
        return try {
            val page = params.key?: 0
            var response: LoadResult<Int, Feed> = LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )

            when(flag){
                HOME_ALL_STORY -> getHomeFeedsUseCase(page, 18).collect {
                    if(it is ResultState.Success) {
                        Log.d(TAG, "getHomeFeedsUseCase collect : ${it.data}")
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
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}