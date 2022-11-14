package com.d205.sdutyplus.view.feed

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.d205.domain.model.mypage.Feed
import com.d205.domain.model.user.User
import com.d205.domain.usecase.feed.GetFeedsUseCase
import com.d205.sdutyplus.uitls.ALL_STORY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okio.BufferedSink
import javax.inject.Inject

private const val TAG ="StoryViewModel"

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedsUseCase: GetFeedsUseCase
): ViewModel() {

    // 모든 스토리 전체 조회
    private fun userFeeds() = Pager(
        config = PagingConfig(pageSize = 1, maxSize = 18, enablePlaceholders = false),
        pagingSourceFactory = {FeedDataSource(ALL_STORY, getFeedsUseCase)}
    ).flow

    val pagingFeedList = getUserFeeds()
//    private val _pagingFeedList : MutableStateFlow<PagingData<Feed>> =
//        MutableStateFlow(PagingData.empty())
//    val pagingFeedList get() = _pagingFeedList.asStateFlow()

    fun getUserFeeds() = userFeeds().cachedIn(viewModelScope)


//    // 스크랩 스토리 전체 조회
//    private fun getScrapList(userSeq: Int) = Pager(
//        config = PagingConfig(pageSize = 1, maxSize = 18, enablePlaceholders = false),
//        pagingSourceFactory = {StoryDataSource(SCRAP_STORY, Retrofit.storyApi, userSeq, 0)}
//    ).liveData
//
//    private val userScrapPage = MutableLiveData<Int>()
//    val pagingScrapList = userScrapPage.switchMap {
//        getScrapList(it).cachedIn(viewModelScope)
//    }
//
//    fun getScrapStoryList(userSeq: Int){
//        userScrapPage.postValue(userSeq)
//    }
//
//
//    fun getUserStoryList(userSeq: Int, writerSeq: Int){
//        val array = IntArray(2)
//        array[0] = userSeq
//        array[1] = writerSeq
//        userStoryPosts.postValue(array)
//    }
//
//    fun insertStory(story: Story, bitmap: Bitmap){
//        viewModelScope.launch(Dispatchers.IO){
//            try {
//                val bitmapRequestBody = bitmap?.let {
//                    BitmapRequestBody(it)
//                }
//
//                var fileName = "story/" + System.currentTimeMillis().toString()+".png"
//                story.imageSource = fileName
////                var requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
//                var imageBody : MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file",fileName, bitmapRequestBody!!)
//                val json = Gson().toJson(story)
//                val storyBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
//                val response = Retrofit.storyApi.insertStory(imageBody, storyBody)
//                if(response.isSuccessful && response.body() != null){
//                    getAllTimelineListValue(story.writerSeq)
//                }
//            }catch (e: Exception){
//                Log.d(TAG, "insertStory: ${e.message}")
//            }
//        }
//    }
//
//    fun updateStory(story: Story){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.updateStory(story).let {
//                if(it.isSuccessful && it.body() != null){
//                    _timeLine.postValue(it.body() as Timeline)
//                }else{
//                    Log.d(TAG, "updateStory: ${it.code()}")
//                }
//            }
//        }
//    }
//
//    fun deleteStory(userSeq: Int, story: Story){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.deleteStory(story.seq).let {
//                if (it.code() == 200) {
//                    getAllTimelineListValue(story.writerSeq)
//                    getStoryPost(userSeq, story.writerSeq)
//                }else if(it.code() == 401){
//
//                }
//                else{
//
//                }
//            }
//        }
//    }
//
//    private val _contributionList = MutableLiveData<List<Boolean>>()
//    val contributionList: LiveData<List<Boolean>>
//        get() = _contributionList
//    fun getContribution(userSeq: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getContributionList(userSeq).let {
//                if(it.isSuccessful && it.body() != null){
//                    _contributionList.postValue(it.body())
//                }else{
//                    Log.d(TAG, "getContribution: ${it.code()}")
//                }
//            }
//        }
//    }

    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/*".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }














    // 모든 타임라인 전체 조회
//    private fun getAllTimelinePage(userSeq: Int) = Pager(
//        config = PagingConfig(pageSize = 1, maxSize = 10, enablePlaceholders = false),
//        pagingSourceFactory = {TimeLineDataSource(ALL_TIMELINE, "","", Retrofit.storyApi, userSeq)}
//    ).liveData
//
//    private val allTimeLinePage = MutableLiveData<Int>()
//    val pagingAllTimelineList = allTimeLinePage.switchMap {
//        getAllTimelinePage(it).cachedIn(viewModelScope)
//    }
//
//    fun getAllTimelineListValue(userSeq: Int){
//        allTimeLinePage.postValue(userSeq)
//    }


    // 직업 태그로 타임라인 전체 조회
//    private var jobName = ""
//    private fun getTimelineJobAndAllPage(userSeq: Int) = Pager(
//        config = PagingConfig(pageSize = 1, maxSize = 10, enablePlaceholders = false),
//        pagingSourceFactory = {TimeLineDataSource(JOB_TIMELINE, jobName,"",Retrofit.storyApi, userSeq)}
//    ).liveData
//
//    private val allTimeLineJobAndAllPage = MutableLiveData<Int>()
//    val pagingTimelineJobAndAllList = allTimeLineJobAndAllPage.switchMap {
//        getTimelineJobAndAllPage(it).cachedIn(viewModelScope)
//    }
//
//    fun getTimelineJobAndAllList(userSeq: Int, jobName: String){
//        this.jobName = jobName
//        allTimeLineJobAndAllPage.postValue(userSeq)
//    }


    // 공부 분야 태그로 타임라인 전체 조회
//    private var interestName = ""
//    private fun getTimelineInterestAndAllPage(userSeq: Int) = Pager(
//        config = PagingConfig(pageSize = 1, maxSize = 10, enablePlaceholders = false),
//        pagingSourceFactory = {TimeLineDataSource(INTEREST_TIMELINE, "", interestName, Retrofit.storyApi, userSeq)}
//    ).liveData
//
//    private val allTimeLineInterestAndAllPage = MutableLiveData<Int>()
//    val pagingTimelineInterestAndAllList = allTimeLineInterestAndAllPage.switchMap {
//        getTimelineInterestAndAllPage(it).cachedIn(viewModelScope)
//    }
//
//    fun getTimelineInterestAndAllList(userSeq: Int, interestName: String){
//        this.interestName = interestName
//        allTimeLineInterestAndAllPage.postValue(userSeq)
//    }




//    private val _timeLine = MutableLiveData<Timeline>()
//    val timeLine: LiveData<Timeline>
//        get() = _timeLine
//
//    fun getTimelineValue(storySeq: Int, userSeq: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getTimelineDetail(storySeq, userSeq).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _timeLine.postValue(it.body() as Timeline)
//                    _replyList.postValue((it.body() as Timeline).replies)
//                }else{
//                    Log.d(TAG, "getStoryValue: ${it.code()}")
//                }
//            }
//        }
//    }



//    private val _replyList = MutableLiveData<MutableList<Reply>>()
//    val replyList: LiveData<MutableList<Reply>>
//        get() = _replyList
//    fun getReplyListValue(storySeq: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getReplyList(storySeq).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _replyList.postValue(it.body() as MutableList<Reply>)
//                }else{
//                    Log.d(TAG, "getReplyListValue: ${it.code()}")
//                }
//            }
//        }
//    }


//    fun insertReply(reply: Reply){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.insertReply(reply).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _replyList.postValue(it.body() as MutableList<Reply>)
//                }else{
//                    Log.d(TAG, "insertReply: ${it.code()}")
//                }
//            }
//        }
//    }


//    fun updateReply(reply: Reply){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.updateReply(reply, reply.storySeq).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _replyList.postValue(it.body() as MutableList<Reply>)
//                }else{
//                    Log.d(TAG, "updateReply: ${it.code()}")
//                }
//            }
//        }
//    }


//    fun deleteReply(reply: Reply){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.deleteReply(reply.seq).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _replyList.postValue(it.body())
//                }else{
//                    Log.d(TAG, "deleteReply: ${it.code()}")
//                }
//            }
//        }
//    }


//    fun likeStory(likes: Likes, userSeq: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.likeStory(likes).let {
//                if (it.code() == 200) {
//                    getTimelineValue(likes.storySeq, userSeq)
//                }else if(it.code() == 401){
//
//                }
//                else{
//
//                }
//            }
//        }
//    }


//    fun likeStoryInTimeLine(likes: Likes){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.likeStory(likes).let {
//                if (it.code() == 200) {
//                }else if(it.code() == 401){
//
//                }
//                else{
//
//                }
//            }
//        }
//    }


//    fun scrapStory(scrap: Scrap){
//        viewModelScope.launch(Dispatchers.IO){
//            try {
//                Retrofit.storyApi.scrapStory(scrap).let {
//                    if (it.code() == 200 && it.body() != null) {
//                        _timeLine.postValue(it.body() as Timeline)
//                    }else if(it.code() == 401){
//                    }
//                    else{
//                    }
//                }
//            }catch (e: Exception){
//                Log.d(TAG, "scrapStory: ${e.message}")
//            }
//        }
//    }


//    fun reportStory(story: Story){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.reportStory(story).let {
//                if (it.code() == 200) {
//
//                }else if(it.code() == 401){
//
//                }
//                else{
//
//                }
//            }
//        }
//    }

//    private val _profile = MutableLiveData<Profile>()
//    val profile: LiveData<Profile>
//        get() = _profile
//    fun getProfileValue(userSeq: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.profileApi.getProfileValue(userSeq).let {
//                if(it.isSuccessful && it.body() != null) {
//                    _profile.postValue(it.body() as Profile)
//                }else{
//                    Log.d(TAG, "getProfileValue: ${it.code()}")
//                }
//            }
//        }
//    }



//    fun blockStory(userSeq: Int, story: Story){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.blockStory(userSeq, story.seq).let {
//                if(it.isSuccessful){
//                    getAllTimelineListValue(userSeq)
//                    getStoryPost(userSeq, story.writerSeq)
//                }else{
//                    Log.d(TAG, "blockStory: ${it.code()}")
//                }
//            }
//        }
//    }

}