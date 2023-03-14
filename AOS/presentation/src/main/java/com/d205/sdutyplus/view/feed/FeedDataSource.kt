package com.d205.sdutyplus.view.feed

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d205.domain.model.feed.Feed
import com.d205.domain.usecase.feed.GetFeedsUseCase
import com.d205.domain.usecase.feed.GetScrapFeedsUseCase
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.utils.ALL_STORY
import com.d205.sdutyplus.utils.SCRAP_STORY

private const val TAG ="FeedDataSource"
class FeedDataSource(
    val flag: Int,
    private val getFeedsUseCase: GetFeedsUseCase,
    private val getScrapFeedsUseCase: GetScrapFeedsUseCase
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
                ALL_STORY -> getFeedsUseCase.invoke(page, 18).collect {
                    if(it is ResultState.Success) {
                        Log.d(TAG, "getFeedsUseCase collect : ${it.data}")
                        response = it.data
                    }
                }
                SCRAP_STORY -> getScrapFeedsUseCase.invoke(page, 18).collect {
                    if(it is ResultState.Success) {
                        Log.d(TAG, "getScrapFeedsUseCase collect : ${it.data}")
                        response = it.data
                    }
                }
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