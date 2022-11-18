package com.d108.sduty.ui.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.d108.sduty.common.*
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.*
import com.d108.sduty.model.paging.StoryDataSource
import com.d108.sduty.model.paging.TimeLineDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink

private const val TAG ="StoryViewModel"
class StoryViewModel: ViewModel() {

    // 모든 타임라인 전체 조회
    private fun getAllTimelinePage(userSeq: Int) = Pager(
        config = PagingConfig(pageSize = 1, maxSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {TimeLineDataSource(ALL_TIMELINE, "","",Retrofit.storyApi, userSeq)}
    ).liveData

    private val allTimeLinePage = MutableLiveData<Int>()
    val pagingAllTimelineList = allTimeLinePage.switchMap {
        getAllTimelinePage(it).cachedIn(viewModelScope)
    }

    fun getAllTimelineListValue(userSeq: Int){
        allTimeLinePage.postValue(userSeq)
    }

    // 직업 태그로 타임라인 전체 조회
    private var jobName = ""
    private fun getTimelineJobAndAllPage(userSeq: Int) = Pager(
        config = PagingConfig(pageSize = 1, maxSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {TimeLineDataSource(JOB_TIMELINE, jobName,"",Retrofit.storyApi, userSeq)}
    ).liveData

    private val allTimeLineJobAndAllPage = MutableLiveData<Int>()
    val pagingTimelineJobAndAllList = allTimeLineJobAndAllPage.switchMap {
        getTimelineJobAndAllPage(it).cachedIn(viewModelScope)
    }

    fun getTimelineJobAndAllList(userSeq: Int, jobName: String){
        this.jobName = jobName
        allTimeLineJobAndAllPage.postValue(userSeq)
    }

    // 공부 분야 태그로 타임라인 전체 조회
    private var interestName = ""
    private fun getTimelineInterestAndAllPage(userSeq: Int) = Pager(
        config = PagingConfig(pageSize = 1, maxSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {TimeLineDataSource(INTEREST_TIMELINE, "", interestName, Retrofit.storyApi, userSeq)}
    ).liveData

    private val allTimeLineInterestAndAllPage = MutableLiveData<Int>()
    val pagingTimelineInterestAndAllList = allTimeLineInterestAndAllPage.switchMap {
        getTimelineInterestAndAllPage(it).cachedIn(viewModelScope)
    }

    fun getTimelineInterestAndAllList(userSeq: Int, interestName: String){
        this.interestName = interestName
        allTimeLineInterestAndAllPage.postValue(userSeq)
    }

    // 팔로우 유저 타임라인 전체 조회
    private fun getTimelineFollowPage(userSeq: Int) = Pager(
        config = PagingConfig(pageSize = 1, maxSize = 10, enablePlaceholders = false),
        pagingSourceFactory = {TimeLineDataSource(FOLLOW_TIMELINE, "","",Retrofit.storyApi, userSeq)}
    ).liveData

    private val timeLineFollowPage = MutableLiveData<Int>()
    val pagingTimelineFollowList = timeLineFollowPage.switchMap {
        getTimelineFollowPage(it).cachedIn(viewModelScope)
    }

    fun getTimelineFollowList(userSeq: Int){
        allTimeLinePage.postValue(userSeq)
    }



    // 스크랩 스토리 전체 조회
    private fun getScrapList(userSeq: Int) = Pager(
        config = PagingConfig(pageSize = 1, maxSize = 18, enablePlaceholders = false),
        pagingSourceFactory = {StoryDataSource(SCRAP_STORY, Retrofit.storyApi, userSeq, 0)}
    ).liveData

    private val userScrapPage = MutableLiveData<Int>()
    val pagingScrapList = userScrapPage.switchMap {
        getScrapList(it).cachedIn(viewModelScope)
    }

    fun getScrapStoryList(userSeq: Int){
        userScrapPage.postValue(userSeq)
    }

    // 모든 스토리 전체 조회
    private fun getStoryPost(userSeq: Int, writerSeq: Int) = Pager(
        config = PagingConfig(pageSize = 1, maxSize = 18, enablePlaceholders = false),
        pagingSourceFactory = {StoryDataSource(ALL_STORY, Retrofit.storyApi, userSeq, writerSeq)}
    ).liveData
    private val userStoryPosts = MutableLiveData<IntArray>()

    val pagingStoryList = userStoryPosts.switchMap {
        getStoryPost(it[0], it[1]).cachedIn(viewModelScope)
    }

    fun getUserStoryList(userSeq: Int, writerSeq: Int){
        val array = IntArray(2)
        array[0] = userSeq
        array[1] = writerSeq
        userStoryPosts.postValue(array)
    }





//    private val _storyList = MutableLiveData<MutableList<Story>>()
//    val storyList: LiveData<MutableList<Story>>
//        get() = _storyList
//
//    private val _timelineList = MutableLiveData<MutableList<Timeline>>()
//    val timelineList: LiveData<MutableList<Timeline>>
//        get() = _timelineList
//    fun getStoryListValue(userSeq: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getStoryList(userSeq).let {
//                if(it.isSuccessful && it.body() != null){
//                    _timelineList.postValue(it.body() as MutableList<Timeline>)
//                }else{
//                    Log.d(TAG, "getStoryListValue: ${it.code()}")
//                }
//            }
//        }
//    }

//    private val _toastMessage = MutableLiveData<String>()
//    val toastMessage: LiveData<String>
//        get() = _toastMessage

    fun insertStory(story: Story, bitmap: Bitmap){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val bitmapRequestBody = bitmap?.let {
                    BitmapRequestBody(it)
                }

                var fileName = "story/" + System.currentTimeMillis().toString()+".png"
                story.imageSource = fileName
//                var requestBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                var imageBody : MultipartBody.Part = MultipartBody.Part.createFormData("uploaded_file",fileName, bitmapRequestBody!!)
                val json = Gson().toJson(story)
                val storyBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json)
                val response = Retrofit.storyApi.insertStory(imageBody, storyBody)
                if(response.isSuccessful && response.body() != null){
                    getAllTimelineListValue(story.writerSeq)
                }
            }catch (e: Exception){
                Log.d(TAG, "insertStory: ${e.message}")
            }
        }
    }

    private val _timeLine = MutableLiveData<Timeline>()
    val timeLine: LiveData<Timeline>
        get() = _timeLine

    fun getTimelineValue(storySeq: Int, userSeq: Int){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.getTimelineDetail(storySeq, userSeq).let {
                if (it.isSuccessful && it.body() != null) {
                    _timeLine.postValue(it.body() as Timeline)
                    _replyList.postValue((it.body() as Timeline).replies)
                }else{
                    Log.d(TAG, "getStoryValue: ${it.code()}")
                }
            }
        }
    }

//    private val _userStoryList = MutableLiveData<List<Story>>()
//    val userStoryList: LiveData<List<Story>>
//        get() = _userStoryList
//    fun getUserStoryListValue(userSeq: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getUserStoryList(userSeq).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _userStoryList.postValue(it.body() as MutableList<Story>)
//                }else{
//                    Log.d(TAG, "getUserStoryListValue: ${it.code()}")
//                }
//            }
//        }
//    }

//    private val _scrapStoryList = MutableLiveData<List<Story>>()
//    val scrapStoryList: LiveData<List<Story>>
//        get() = _scrapStoryList
//    fun getScrapStoryListValue(userSeq: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getScrapList(userSeq).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _scrapStoryList.postValue(it.body() as MutableList<Story>)
//                }else{
//                    Log.d(TAG, "getScrapStoryListValue: ${it.code()}")
//                }
//            }
//        }
//    }

//    private val _filteredTimelineList = MutableLiveData<MutableList<Timeline>>()
//    val filteredTimelineList: LiveData<MutableList<Timeline>>
//        get() = _filteredTimelineList
//    fun getTimelineJobFollowList(userSeq: Int, jobName: String){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getStoryJobAndFollowList(userSeq, jobName).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _filteredTimelineList.postValue(it.body() as MutableList<Timeline>)
//                }else{
//                    Log.d(TAG, "getTimelineJobFollowList: ${it.code()}")
//                }
//            }
//        }
//    }

//    fun getTimelineJobAllList(userSeq: Int, jobName: String){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getStoryJobAndAllList(userSeq, jobName).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _filteredTimelineList.postValue(it.body() as MutableList<Timeline>)
//                }else{
//                    Log.d(TAG, "getTimelineJobAllList: ${it.code()}")
//                }
//            }
//        }
//    }
//
//    fun getTimelineInterestList(userSeq: Int, jobName: String){
//        viewModelScope.launch(Dispatchers.IO){
//            Retrofit.storyApi.getStoryInterestAndAllList(userSeq, jobName).let {
//                if (it.isSuccessful && it.body() != null) {
//                    _filteredTimelineList.postValue(it.body() as MutableList<Timeline>)
//                }else{
//                    Log.d(TAG, "getTimelineInterestList: ${it.code()}")
//                }
//            }
//        }
//    }

    fun updateStory(story: Story){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.updateStory(story).let {
                if(it.isSuccessful && it.body() != null){
                    _timeLine.postValue(it.body() as Timeline)
                }else{
                    Log.d(TAG, "updateStory: ${it.code()}")
                }
            }
        }
    }

    fun deleteStory(userSeq: Int, story: Story){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.deleteStory(story.seq).let {
                if (it.code() == 200) {
                    getAllTimelineListValue(story.writerSeq)
                    getStoryPost(userSeq, story.writerSeq)
                }else if(it.code() == 401){

                }
                else{

                }
            }
        }
    }

    private val _replyList = MutableLiveData<MutableList<Reply>>()
    val replyList: LiveData<MutableList<Reply>>
        get() = _replyList
    fun getReplyListValue(storySeq: Int){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.getReplyList(storySeq).let {
                if (it.isSuccessful && it.body() != null) {
                    _replyList.postValue(it.body() as MutableList<Reply>)
                }else{
                    Log.d(TAG, "getReplyListValue: ${it.code()}")
                }
            }
        }
    }

    fun insertReply(reply: Reply){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.insertReply(reply).let {
                if (it.isSuccessful && it.body() != null) {
                    _replyList.postValue(it.body() as MutableList<Reply>)
                }else{
                    Log.d(TAG, "insertReply: ${it.code()}")
                }
            }
        }
    }

    fun updateReply(reply: Reply){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.updateReply(reply, reply.storySeq).let {
                if (it.isSuccessful && it.body() != null) {
                    _replyList.postValue(it.body() as MutableList<Reply>)
                }else{
                    Log.d(TAG, "updateReply: ${it.code()}")
                }
            }
        }
    }

    fun deleteReply(reply: Reply){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.deleteReply(reply.seq).let {
                if (it.isSuccessful && it.body() != null) {
                    _replyList.postValue(it.body())
                }else{
                    Log.d(TAG, "deleteReply: ${it.code()}")
                }
            }
        }
    }

    fun likeStory(likes: Likes, userSeq: Int){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.likeStory(likes).let {
                if (it.code() == 200) {
                    getTimelineValue(likes.storySeq, userSeq)
                }else if(it.code() == 401){

                }
                else{

                }
            }
        }
    }

    fun likeStoryInTimeLine(likes: Likes){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.likeStory(likes).let {
                if (it.code() == 200) {
                }else if(it.code() == 401){

                }
                else{

                }
            }
        }
    }

    fun scrapStory(scrap: Scrap){
        viewModelScope.launch(Dispatchers.IO){
            try {
                Retrofit.storyApi.scrapStory(scrap).let {
                    if (it.code() == 200 && it.body() != null) {
                        _timeLine.postValue(it.body() as Timeline)
                    }else if(it.code() == 401){
                    }
                    else{
                    }
                }
            }catch (e: Exception){
                Log.d(TAG, "scrapStory: ${e.message}")
            }
        }
    }

    fun reportStory(story: Story){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.reportStory(story).let {
                if (it.code() == 200) {

                }else if(it.code() == 401){

                }
                else{

                }
            }
        }
    }

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile>
        get() = _profile
    fun getProfileValue(userSeq: Int){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.profileApi.getProfileValue(userSeq).let {
                if(it.isSuccessful && it.body() != null) {
                    _profile.postValue(it.body() as Profile)
                }else{
                    Log.d(TAG, "getProfileValue: ${it.code()}")
                }
            }
        }
    }

    private val _isFollowSucceed = MutableLiveData<Boolean>(false)
    val isFollowSucceed: LiveData<Boolean>
        get() = _isFollowSucceed
    fun doFollow(follow: Follow){
        viewModelScope.launch(Dispatchers.IO) {
            Retrofit.profileApi.doFollow(follow).let {
                if (it.isSuccessful) {
                    _isFollowSucceed.postValue(!_isFollowSucceed.value!!)
                } else {
                    Log.d(TAG, "doFollow: ${it.code()}")
                }
            }
        }
    }

    private val _contributionList = MutableLiveData<List<Boolean>>()
    val contributionList: LiveData<List<Boolean>>
        get() = _contributionList
    fun getContribution(userSeq: Int){  
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.getContributionList(userSeq).let { 
                if(it.isSuccessful && it.body() != null){
                    _contributionList.postValue(it.body())
                }else{
                    Log.d(TAG, "getContribution: ${it.code()}")
                }
            }
        }
    }

    fun blockStory(userSeq: Int, story: Story){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.storyApi.blockStory(userSeq, story.seq).let {
                if(it.isSuccessful){
                    getAllTimelineListValue(userSeq)
                    getStoryPost(userSeq, story.writerSeq)
                }else{
                    Log.d(TAG, "blockStory: ${it.code()}")
                }
            }
        }
    }

    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/*".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 99, sink.outputStream())
        }
    }

}