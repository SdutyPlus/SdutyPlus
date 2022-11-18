package com.d108.sduty.ui.sign.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d108.sduty.common.ApplicationClass
import com.d108.sduty.common.FIND_ID
import com.d108.sduty.common.FIND_PW
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest

private const val TAG ="FindInfoViewModel"
class FindInfoViewModel: ViewModel() {
    private val _flagInfo = MutableLiveData<String>("아이디 찾기")
    val flagInfo: LiveData<String>
        get() = _flagInfo
    fun setFlag(flag: Int){
        when(flag){
            FIND_ID -> _flagInfo.postValue("아이디 찾기")
            FIND_PW -> _flagInfo.postValue("비밀번호 찾기")
        }
    }

    private val _isSucceedFindId = MutableLiveData<Boolean>()
    val isSucceedFindId: LiveData<Boolean>
        get() = _isSucceedFindId

    fun findId(name:String, tel: String){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.userApi.findId(name, tel).let {
                if(it.isSuccessful && it.body() != null){
                    userId = it.body().toString()
                    sendSMS(tel)
                    _isSucceedFindId.postValue(true)
                }else{
                    Log.d(TAG, "findId: ${it.code()}")
                    _isSucceedFindId.postValue(false)
                }

            }
        }
    }

    private val _isSucceedChangePw = MutableLiveData<Boolean>()
    val isSucceedChangePw: LiveData<Boolean>
        get() = _isSucceedChangePw
    fun changePw(user: User){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.userApi.changePw(user).let {
                if(it.isSuccessful){
                    _isSucceedChangePw.postValue(true)
                }
                else{
                    _isSucceedChangePw.postValue(false)
                }

            }
        }
    }

    var userId = ""
    private fun sendSMS(tel: String){
        val message = Message(
            from = "01049177914",
            to = tel,
            text = "[Sduty] 가입하신 아이디는 [${userId}]입니다."
        )
        ApplicationClass.messageService.sendOne(SingleMessageSendingRequest(message))
    }



}