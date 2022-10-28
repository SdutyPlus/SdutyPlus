package com.d108.sduty.model.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.d108.sduty.common.ALL_TIMELINE
import com.d108.sduty.common.FOLLOW_TIMELINE
import com.d108.sduty.common.INTEREST_TIMELINE
import com.d108.sduty.common.JOB_TIMELINE
import com.d108.sduty.model.api.StoryApi
import com.d108.sduty.model.dto.Timeline
import retrofit2.Response

private const val TAG ="TimeLineDataSource"
class TimeLineDataSource(
    val flag: Int,
    private val jobName: String,
    private val interestName: String,
    private val storyApi: StoryApi,
    private val userSeq: Int


    ): PagingSource<Int, Timeline>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Timeline> {
        return try{

            val page = params.key?: 0
            var response: Response<PagingResult<Timeline>>? = null

            when(flag){
                ALL_TIMELINE -> response = storyApi.getAllPagingTimeline(userSeq, page)
                JOB_TIMELINE -> response = storyApi.getStoryJobAndAllList(userSeq, jobName, page)
                INTEREST_TIMELINE -> response = storyApi.getStoryInterestAndAllList(userSeq, interestName, page)
                FOLLOW_TIMELINE -> response = storyApi.getAllTimelineFollowList(userSeq, page)
            }
            val body = response!!.body() as PagingResult<Timeline>

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
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Timeline>): Int? {
        TODO("Not yet implemented")
    }
}