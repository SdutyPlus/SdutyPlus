package com.d108.sduty.ui.main.mypage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.Follow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG ="FollowViewModel"
class FollowViewModel: ViewModel() {
    private val _followerList = MutableLiveData<MutableList<Follow>>()
    val followerList: LiveData<MutableList<Follow>>
        get() = _followerList

    fun getFollower(userSeq: Int){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.profileApi.getFollower(userSeq).let {
                if(it.isSuccessful && it.body() != null){
                    _followerList.postValue(it.body() as MutableList<Follow>)
                }else{
                    Log.d(TAG, "getFollower: ${it.code()}")
                }
            }
        }
    }

    private val _followeeList = MutableLiveData<MutableList<Follow>>()
    val followeeList: LiveData<MutableList<Follow>>
        get() = _followeeList

    fun getFollowee(userSeq: Int){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.profileApi.getFollowee(userSeq).let {
                if(it.isSuccessful && it.body() != null){
                    _followeeList.postValue(it.body() as MutableList<Follow>)
                }else{
                    Log.d(TAG, "getFollowee: ${it.code()}")
                }
            }
        }
    }

    private val _isMyFollowPage = MutableLiveData<Boolean>()
    val isMyFollowPage: LiveData<Boolean>
        get() = _isMyFollowPage
    fun setMyFollowPage(myFollowPage: Boolean){
        _isMyFollowPage.postValue(myFollowPage)
    }
}