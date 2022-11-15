package com.d108.sduty.ui.camstudy.preview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d108.sduty.utils.Resource
import com.d108.sduty.utils.Status
import com.sendbird.calls.*
import com.sendbird.calls.handler.AuthenticateHandler
import com.sendbird.calls.handler.CompletionHandler
import com.sendbird.calls.handler.RoomHandler

private const val TAG ="PreviewViewModel"
class PreviewViewModel: ViewModel() {
    private val _enterResult: MutableLiveData<Resource<Unit>> = MutableLiveData()
    val enterResult: LiveData<Resource<Unit>> = _enterResult

    fun enter(roomId: String, isAudioEnabled: Boolean, isVideoEnabled: Boolean) {
        val room = SendBirdCall.getCachedRoomById(roomId) ?: return

        _enterResult.postValue(Resource.loading(null))
        val enterParams = EnterParams()
            .setAudioEnabled(isAudioEnabled)
            .setVideoEnabled(isVideoEnabled)

        room.enter(enterParams, object : CompletionHandler {
            override fun onResult(e: SendBirdException?) {
                Log.d(TAG, "onResult: ${e.toString()}")
                if (e == null) {
                    _enterResult.postValue(Resource.success(null))
                } else {
                    _enterResult.postValue(Resource.error(e.message, e.code, null))
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

    private val _fetchedRoomId: MutableLiveData<Resource<String>> = MutableLiveData()
    val fetchedRoomId: LiveData<Resource<String>> = _fetchedRoomId

    fun fetchRoomById(roomId: String) {
        if (roomId.isEmpty()) {
            return
        }

        if (_fetchedRoomId.value?.status == Status.LOADING) {
            return
        }

        _fetchedRoomId.postValue(Resource.loading(null))
        SendBirdCall.fetchRoomById(roomId, object : RoomHandler {
            override fun onResult(room: Room?, e: SendBirdException?) {
                if (e != null) {
                    _fetchedRoomId.postValue(Resource.error(e.message, e.code, null))
                } else {
                    _fetchedRoomId.postValue(Resource.success(room?.roomId))
                }
            }
        })
    }
}