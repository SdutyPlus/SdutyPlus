package com.d108.sduty.ui.sign.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d108.sduty.common.ApplicationClass
import com.d108.sduty.common.COMMON_JOIN
import com.d108.sduty.common.KAKAO_JOIN
import com.d108.sduty.common.NAVER_JOIN
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.AuthInfo
import com.d108.sduty.model.dto.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.nurigo.sdk.message.model.Message
import net.nurigo.sdk.message.request.SingleMessageSendingRequest
import java.util.regex.Pattern

private const val TAG ="JoinViewModel"
class JoinViewModel: ViewModel() {
    private val _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String>
        get() = _accessToken

    fun setToken(token: String){
        _accessToken.value = token
    }

    private val _isJoinSucced = MutableLiveData<Boolean>()
    val isJoinSucced: LiveData<Boolean>
        get() = _isJoinSucced

    fun join(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Retrofit.userApi.join(user).let {
                    if(it.isSuccessful){
                        _isJoinSucced.postValue(true)
                    }
                    else if (it.code() == 500) {
                        _isJoinSucced.postValue(false)
                    }
                }
            } catch (e: Exception){
                Log.d("TAG", "login: error ${e.message}")
            }
        }
    }

    private val _isUsedId = MutableLiveData<Boolean>(false)
    val isUsedId: LiveData<Boolean>
        get() = _isUsedId
    fun getUsedId(id: String){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.userApi.getUsedId(id).let {
                if(it.code() == 401){
                    _isUsedId.postValue(false)
                }else if(it.code() == 200){
                    _isUsedId.postValue(true)
                }else{
                    Log.d(TAG, "getUsedId: ${it.code()}")
                }
            }
        }
    }

    private val _socialUser = MutableLiveData<User>()
    val socialUser: LiveData<User>
        get() = _socialUser
    fun kakaoUserInfo(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                Retrofit.userApi.kakaoUserInfo(accessToken.value.toString()).let {
                    if(it.isSuccessful && it.body() != null){
                        _socialUser.postValue(it.body() as User)
                        _isSelfInputEmail.postValue(true)
                    }
                }
            }catch (e: Exception){
                Log.d(TAG, "kakaoJoin: ${e.message}")
            }
        }
    }
    fun naverUserInfo(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                Retrofit.userApi.naverUserInfo(accessToken.value.toString()).let {
                    if(it.isSuccessful && it.body() != null){
                        _socialUser.postValue(it.body() as User)
                        _isSelfInputEmail.postValue(true)
                    }
                }
            }catch (e: Exception){
                Log.d(TAG, "kakaoJoin: ${e.message}")
            }
        }
    }


    private val _authInfo = MutableLiveData<AuthInfo>()
    val authInfo: LiveData<AuthInfo>
        get() = _authInfo

    private val _isSucceedSendAuthInfo = MutableLiveData<Boolean>()
    val isSucceedSendAuthInfo : LiveData<Boolean>
        get() = _isSucceedSendAuthInfo

    fun sendOTP(userPhoneNum: String){
        viewModelScope.launch(Dispatchers.IO){
            var authCode = (100000..999999).random()
            val message = Message(
                from = "01049177914",
                to = userPhoneNum,
                text = "[Sduty] 인증 번호[${authCode}]를 입력해 주세요. "
            )
            val newAuthInfo = AuthInfo(userPhoneNum, authCode.toString(), )
            _authInfo.postValue(newAuthInfo)
            ApplicationClass.messageService.sendOne(SingleMessageSendingRequest(message)) // SMS 보냄
            Retrofit.userApi.sendAuthInfo(newAuthInfo).let {
                if(it.code() == 200){
                    _isSucceedSendAuthInfo.postValue(true)
                }else{
                    _isSucceedSendAuthInfo.postValue(false)
                }
            }
        }
    }

    private val _isSucceedAuth = MutableLiveData<Boolean>(false)
    val isSucceedAuth : LiveData<Boolean>
        get() = _isSucceedAuth
    private val _authMsg = MutableLiveData<String>("")
    val authMsg : LiveData<String>
        get() = _authMsg
    fun checkOTP(inputCode: String){
        viewModelScope.launch(Dispatchers.IO){
            val code = authInfo.value
            code!!.code = inputCode
            Retrofit.userApi.checkAuthCode(code).let {
                if(it.code() == 401){
                    _isSucceedAuth.postValue(false)
                    _authMsg.postValue("인증번호가 일치하지 않습니다.")
                }
                else if(it.code() == 410){
                    _isSucceedAuth.postValue(false)
                    _authMsg.postValue("인증 시간이 만료되었습니다.")
                }
                else if(it.isSuccessful){
                    _isSucceedAuth.postValue(true)
                    _authMsg.postValue("인증 되었습니다.")
                }
            }
        }
    }

    private val _isWrongPW = MutableLiveData<Boolean>()
    val isWrongPW: LiveData<Boolean>
        get() = _isWrongPW
    fun checkPW(pw1: String, pw2: String){
        _isWrongPW.value = pw1 != pw2
    }

    private val _isShortPW = MutableLiveData<Boolean>(false)
    val isShortPW: LiveData<Boolean>
        get() = _isShortPW
    fun checkPWLength(pw: String){
        _isShortPW.value = pw.length < 8
    }

    private val _isWrongName = MutableLiveData<Boolean>(false)
    val isWrongName: LiveData<Boolean>
        get() = _isWrongName
    fun checkName(name: String){
        val ps = Pattern.compile("^[a-zA-Z가-힣]+\$")
        _isWrongName.value = !ps.matcher(name).matches()

    }


    private val _isSocialJoin = MutableLiveData<Boolean>()
    val isSocialJoin : LiveData<Boolean>
        get() = _isSocialJoin
    private val _isNaverJoin = MutableLiveData<Boolean>(false)
    val isNaverJoin : LiveData<Boolean>
        get() = _isNaverJoin

    fun setJoinFlag(joinFlag: Int){
        when(joinFlag){
            COMMON_JOIN ->{
                _isSocialJoin.value = false
            }
            KAKAO_JOIN ->{
                _isSocialJoin.value = true
            }
            NAVER_JOIN ->{
                _isSocialJoin.value = true
                _isNaverJoin.value = true
            }
        }
    }

    private val _isSelfInputEmail = MutableLiveData<Boolean>()
    val isSelfInputEmail: LiveData<Boolean>
        get() = _isSelfInputEmail
    fun setSelfInput(){
        _isSelfInputEmail.postValue(true)
    }

}