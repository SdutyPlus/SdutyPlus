package com.d108.sduty.ui.main.mypage.setting.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.Notice
import com.d108.sduty.model.dto.Qna
import com.d108.sduty.model.dto.User
import com.d108.sduty.utils.SettingsPreference
import com.d108.sduty.utils.sharedpreference.FCMPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG ="SettingViewModel"
class SettingViewModel: ViewModel() {
    private val _isSucceedResign = MutableLiveData<Boolean>()
    val isSucceedResign: LiveData<Boolean>
        get() = _isSucceedResign
    fun resignUser(user: User){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.userApi.deleteUser(user.seq).let {
                if(it.isSuccessful){
                    _isSucceedResign.postValue(true)
                }else{
                    _isSucceedResign.postValue(false)
                }
            }
        }
    }


    fun updateFCMToken(user: User){
        viewModelScope.launch(Dispatchers.IO){
            try {
                user.fcmToken = FCMPreference().getFcmToken()
                user.userPublic = SettingsPreference().getPushState()
                user.regtime = null
                Retrofit.userApi.updateUser(user).let {
                    if(it.isSuccessful){
                        Log.d(TAG, "updateFCMToken: ${it.code()}")
                    }else{
                        Log.d(TAG, "updateFCMToken: ${it.code()}")
                    }
                }

            }catch (e: Exception){
                Log.d(TAG, "updateFCMToken: ${e.message}")
            }
        }
    }


    fun insertQna(qna: Qna){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.settingApi.insertQna(qna).let {
                if(it.isSuccessful){

                }else{
                    Log.d(TAG, "insertQna: ${it.code()}")
                }
            }
        }
    }

    private val _qnaList = MutableLiveData<MutableList<Qna>>()
    val qnaList: LiveData<MutableList<Qna>>
        get() = _qnaList
    fun getQnaList(userSeq: Int){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.settingApi.getQnaList(userSeq).let {
                if(it.isSuccessful && it.body() != null){
                    _qnaList.postValue(it.body())
                    Log.d(TAG, "getQnaList: ${it.body()}")
                }else{
                    Log.d(TAG, "getQnaList: ${it.code()}")
                }
            }
        }
    }

    private val _noticeList = MutableLiveData<List<Notice>>()
    val noticeList: LiveData<List<Notice>>
        get() = _noticeList
    fun getNoticeList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.settingApi.getNoticeList()
                if(response.isSuccessful && response.body() != null){
                    _noticeList.postValue(response.body() as List<Notice>)
                }
            } catch (e: java.lang.Exception){
                Log.d(TAG, "getNoticeList: ${e.message}")
            }
        }
    }
}