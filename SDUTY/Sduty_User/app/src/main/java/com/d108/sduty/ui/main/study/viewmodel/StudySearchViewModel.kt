package com.d108.sduty.ui.main.study.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.Study
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "StudySearchViewModel"
class StudySearchViewModel: ViewModel() {
    private val _studySearchList = MutableLiveData<List<Study>>()
    val studySearchList: LiveData<List<Study>>
        get() = _studySearchList

    fun getStudySearchList(keyword: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 내 스터디 리스트 불러오기
                val response = Retrofit.studyApi.studySearch(keyword)
                Log.d(TAG, "getStudySearchList: ${response}")
                if(response.isSuccessful && response.body() != null){
                    _studySearchList.postValue(response.body() as List<Study>)
                }
            }catch (e: Exception){
                Log.d(TAG, "getList: ${e.message}")
            }
        }
    }

    private val _isJoinStudySuccess = MutableLiveData<Boolean>()
    val isJoinStudySuccess: LiveData<Boolean>
        get() = _isJoinStudySuccess

    fun studyJoin(studySeq: Int, userSeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.studyApi.studyJoin(studySeq, userSeq)
                if(response.code() == 200){
                    _isJoinStudySuccess.postValue(true)
                }
                else if (response.code() == 403) {
                    _isJoinStudySuccess.postValue(false)
                }

            } catch (e: Exception){
                Log.d(TAG, "studyJoin: ${e.message}")
            }
        }
    }

    private val _studyDetail = MutableLiveData<Study>()
    val studyDetail: LiveData<Study>
        get() = _studyDetail
    // 스터디 상세 조회
    fun studyDetail(studySeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.studyApi.studyDetail(studySeq)
                if(response.isSuccessful && response.body() != null){
                    _studyDetail.postValue(response.body() as Study)
                }
            } catch (e: Exception){
                Log.d(TAG, "studyDetail: ${e.message}")
            }
        }
    }
}