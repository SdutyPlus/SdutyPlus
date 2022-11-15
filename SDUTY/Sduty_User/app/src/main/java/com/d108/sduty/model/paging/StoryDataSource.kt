package com.d108.sduty.model.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d108.sduty.common.ALL_STORY
import com.d108.sduty.common.SCRAP_STORY
import com.d108.sduty.model.api.StoryApi
import com.d108.sduty.model.dto.Story
import retrofit2.Response

private const val TAG ="TimeLineDataSource"
class StoryDataSource(val flag: Int, private val storyApi: StoryApi, private val userSeq: Int, private val writerSeq: Int): PagingSource<Int, Story>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try{
            val page = params.key?: 0
            var response: Response<PagingResult<Story>>? = null

            when(flag){
                ALL_STORY -> response = storyApi.getUserStoryList(writerSeq, userSeq, page, 18)
                SCRAP_STORY -> response = storyApi.getScrapList(userSeq, page, 18)
            }
            val body = response!!.body() as PagingResult<Story>
            if(response.isSuccessful && body.result.isNotEmpty()) {
                LoadResult.Page(
                    data = body.result,
                    prevKey = if(page == 0) null else page -1,
                    nextKey = if(page == body.totalPage) null else page +1
                )
            } else {
                LoadResult.Page(
                    data = body.result,
                    prevKey = null,
                    nextKey = null
                )
            }
        } catch (e: Exception) {
            Log.d(TAG, "load: ${e.message}")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        TODO("Not yet implemented")
    }
}