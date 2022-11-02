package com.d205.sdutyplus.view.join

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.UserDto
import com.d205.domain.usecase.user.AddKakaoUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

private const val TAG = "JoinViewModel"
class JoinViewModel @Inject constructor(
    private val addKakaoUserUseCase: AddKakaoUserUseCase
): ViewModel() {
    private val _isUsedId = MutableLiveData(false)
    val isUsedId: LiveData<Boolean>
        get() = _isUsedId

    private val _isIdCorrectPattern = MutableLiveData(true)
    val isIdCorrectPattern: LiveData<Boolean>
        get() = _isIdCorrectPattern

    private val _isSamePassword = MutableLiveData(false)
    val isSamePassword: LiveData<Boolean>
        get() = _isSamePassword

    private val _id = MutableLiveData<String>()
    val id: LiveData<String>
        get() = _id

    fun checkUsedId(id : String) {
        val flag = true
        _isUsedId.value = false
    }

    fun checkIdPatternCorrect(id : String) {
        val result = isCorrectEmailPattern(id)
        Log.d(TAG, "isCorrectEmailPattern : $result")
        _isIdCorrectPattern.postValue(result)
    }

    fun setPasswordSame(flag: Boolean) {
        Log.d(TAG, "setPasswordSame: $flag")
        _isSamePassword.postValue(flag)
    }

    private fun isCorrectEmailPattern(name: String): Boolean {
        if (name.isEmpty())
            return true

        return Patterns.EMAIL_ADDRESS.matcher(name).matches()
    }

    suspend fun addKakaoUser(userDto: UserDto): Boolean {
        val result = viewModelScope.async {
            addKakaoUserUseCase.execute(userDto)
        }
        return result.await()
    }
















    private val _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String>
        get() = _accessToken

    fun setToken(token: String){
        _accessToken.value = token
    }

    private val _isJoinSucceeded = MutableLiveData<Boolean>()
    val isJoinSucceeded: LiveData<Boolean>
        get() = _isJoinSucceeded

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

    private val _isSelfInputEmail = MutableLiveData<Boolean>()
    val isSelfInputEmail: LiveData<Boolean>
        get() = _isSelfInputEmail

    fun setSelfInput(){
        _isSelfInputEmail.postValue(true)
    }

    private val _isSocialJoin = MutableLiveData<Boolean>()
    val isSocialJoin : LiveData<Boolean>
        get() = _isSocialJoin
    private val _isNaverJoin = MutableLiveData<Boolean>(false)
    val isNaverJoin : LiveData<Boolean>
        get() = _isNaverJoin


    private val _isSucceedSendAuthInfo = MutableLiveData<Boolean>()
    val isSucceedSendAuthInfo : LiveData<Boolean>
        get() = _isSucceedSendAuthInfo

    private val _isSucceedAuth = MutableLiveData<Boolean>(false)
    val isSucceedAuth : LiveData<Boolean>
        get() = _isSucceedAuth
    private val _authMsg = MutableLiveData<String>("")
    val authMsg : LiveData<String>
        get() = _authMsg
}