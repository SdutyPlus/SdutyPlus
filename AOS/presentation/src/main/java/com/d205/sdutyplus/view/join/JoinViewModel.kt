package com.d205.sdutyplus.view.join

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.regex.Pattern

private const val TAG = "JoinViewModel"
class JoinViewModel: ViewModel() {
    private val _isUsedId = MutableLiveData<Boolean>(false)
    val isUsedId: LiveData<Boolean>
        get() = _isUsedId

    private val _isCorrectId = MutableLiveData<Boolean>(true)
    val isCorrectId: LiveData<Boolean>
        get() = _isCorrectId

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

    fun checkIdCorrect(id : String) {
        val result = isCorrectId(id)
        Log.d(TAG, "checkCorrectId: $result")
        _isCorrectId.postValue(result)
    }

    fun setPasswordSame(flag: Boolean) {
        Log.d(TAG, "setPasswordSame: $flag")
        _isSamePassword.postValue(flag)
    }

    private fun isCorrectId(name: String): Boolean {
        if (name.isEmpty())
            return true

        return Patterns.EMAIL_ADDRESS.matcher(name).matches()
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