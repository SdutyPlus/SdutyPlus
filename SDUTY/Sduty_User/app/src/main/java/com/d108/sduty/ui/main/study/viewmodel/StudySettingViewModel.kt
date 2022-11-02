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

private const val TAG = "StudySettingViewModel"
class StudySettingViewModel: ViewModel() {
    private val _isQuitStudy = MutableLiveData<Boolean>()
    val isQuitStudy: LiveData<Boolean>
        get() = _isQuitStudy

    private val _isDeleteStudy = MutableLiveData<Boolean>()
    val isDeleteStudy: LiveData<Boolean>
        get() = _isDeleteStudy

    private val _studyDetail = MutableLiveData<Study>()
    val studyDetail: LiveData<Study>
        get() = _studyDetail

    private val _isStudyUpdate = MutableLiveData<Boolean>()
    val isStudyUpdate: LiveData<Boolean>
        get() = _isStudyUpdate

    private val _myStudyInfo = MutableLiveData<Map<String, Any>>()
    val myStudyInfo: LiveData<Map<String, Any>>
        get() = _myStudyInfo

    private val _isStudyId = MutableLiveData<Boolean>()
    val isStudyId: LiveData<Boolean>
        get() = _isStudyId

    fun getMyStudyInfo(userSeq: Int, studySeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 내 스터디 리스트 불러오기
                val response = Retrofit.studyApi.getMyStudyInfo(userSeq, studySeq)
                if(response.isSuccessful && response.body() != null){
                    _myStudyInfo.postValue(response.body() as Map<String, Any>)
                }

            }catch (e: Exception){
                Log.d(TAG, "getList: ${e.message}")
            }
        }
    }

    // 스터디 수정
    fun studyUpdate(userSeq: Int, studySeq: Int, study: Study){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.studyApi.studyUpdate(userSeq, studySeq, study)
                if(response.isSuccessful){
                    _isStudyUpdate.postValue(true)
                } else {
                    _isStudyUpdate.postValue(false)
                }

            } catch (e: Exception){
                Log.d(TAG, "studyUpdate: ${e.message}")
            }
        }
    }

    // 스터디 탈퇴 및 강퇴
    fun studyQuit(studySeq: Int, userSeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.studyApi.studyQuit(studySeq, userSeq)
                if(response.isSuccessful){
                    _isQuitStudy.postValue(true)
                }
                else if(response.code() == 500){
                    _isQuitStudy.postValue(false)
                }

            }catch (e: Exception){
                Log.d(TAG, "getList: ${e.message}")
            }
        }
    }

    // 스터디 삭제
    fun studyDelete(userSeq: Int, studySeq: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.studyApi.studyDelete(userSeq, studySeq)
                if(response.isSuccessful){
                    _isDeleteStudy.postValue(true)
                }
                else if(response.code() == 403){
                    _isDeleteStudy.postValue(false)
                }

            } catch (e: Exception){
                Log.d(TAG, "deleteStudy: ${e.message}")
            }
        }
    }

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

    fun getStudyId(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.studyApi.getStudyName(id)
                if(response.code() == 200){
                    _isStudyId.postValue(false)
                }
                else if(response.code() == 401){
                    _isStudyId.postValue(true)
                }
            }catch (e: Exception){
                Log.d(TAG, "getStudyId: ${e.message}")
            }
        }
    }



}