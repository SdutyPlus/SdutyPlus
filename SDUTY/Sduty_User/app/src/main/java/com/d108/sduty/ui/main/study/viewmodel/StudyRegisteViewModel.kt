package com.d108.sduty.ui.main.study.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.Alarm
import com.d108.sduty.model.dto.Study
import com.d108.sduty.utils.Resource
import com.d108.sduty.utils.Status
import com.sendbird.calls.*
import com.sendbird.calls.handler.AuthenticateHandler
import com.sendbird.calls.handler.CompletionHandler
import com.sendbird.calls.handler.RoomHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "StudyRegistViewModel"

class StudyRegisteViewModel: ViewModel() {
    private val _study = MutableLiveData<Study>()
    val study: LiveData<Study>
        get() = _study

    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    private val _isStudyId = MutableLiveData<Boolean>()
    val isStudyId: LiveData<Boolean>
        get() = _isStudyId

    private val _createRoomId: MutableLiveData<Resource<String>> = MutableLiveData()
    val createRoomId: LiveData<Resource<String>> = _createRoomId

    fun studyCreate(study: Study){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Retrofit.studyApi.studyCreate(mapOf("Study" to study)).let {
                    if(it.isSuccessful){
                        _createSuccess.postValue(true)
                    }
                    else if (it.code() == 500) {
                        _createSuccess.postValue(false)
                    }
                }
            } catch (e: Exception){
                Log.d(TAG, "error: ${e.message}")
            }
        }
    }

    fun createRoom(){
        if(_createRoomId.value?.status == Status.LOADING){
            return
        }

        _createRoomId.postValue(Resource.loading(null))

        val params = RoomParams(RoomType.SMALL_ROOM_FOR_VIDEO)
        SendBirdCall.createRoom(params, object : RoomHandler{
            override fun onResult(room: Room?, e: SendBirdException?) {
                if (e != null){
                    _createRoomId.postValue(Resource.error(e.message, e.code, null))
                } else{
                    room?.enter(EnterParams().setAudioEnabled(false).setVideoEnabled(false), object : CompletionHandler{
                        override fun onResult(e: SendBirdException?) {
                            if (e != null){
                                _createRoomId.postValue(Resource.error(e.message, e.code, null))
                            } else{
                                _createRoomId.postValue(Resource.success(room.roomId))
                                room.exit()

                            }
                        }

                    })
                }
            }

        })
    }

    private val _authenticateLiveData: MutableLiveData<Resource<User>> = MutableLiveData()
    val authenticationLiveData: LiveData<Resource<User>> = _authenticateLiveData

    fun authenticate(userId: String) {
        _authenticateLiveData.postValue(Resource.loading(null))
        if (userId.isEmpty()) {
            _authenticateLiveData.postValue(Resource.error("User ID is empty", null, null))
            return
        }

        val authenticateParams = AuthenticateParams(userId)

        SendBirdCall.authenticate(authenticateParams, object : AuthenticateHandler {
            override fun onResult(user: User?, e: SendBirdException?) {
                val resource = if (e == null) {
                    Resource.success(user)
                } else {
                    Resource.error(e.message, e.code, null)
                }
                _authenticateLiveData.postValue(resource)
            }
        })
    }

    fun camStudyCreate(study: Study, alarm: Alarm){
        viewModelScope.launch(Dispatchers.IO) {
            val studyObj: Map<String, Objects> = mapOf("Study" to study, "Alarm" to alarm) as Map<String, Objects>
            try {
                Retrofit.studyApi.camStudyCreate(studyObj).let {
                    if(it.isSuccessful){
                        _createSuccess.postValue(true)
                    }
                    else if (it.code() == 500) {
                        _createSuccess.postValue(false)
                    }
                }
            } catch (e: Exception){
                Log.d(TAG, "error: ${e.message}")
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