package com.d205.sdutyplus.view.join

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.User
import com.d205.domain.model.user.UserDto
import com.d205.domain.usecase.user.JoinUserUseCase
import com.d205.domain.usecase.user.UpdateUserUseCase
import com.d205.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.math.log

private const val TAG = "JoinViewModel"

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val joinUserUseCase: JoinUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
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

    private val _isJoinSucceeded = MutableLiveData(false)
    val isJoinSucceeded: LiveData<Boolean>
        get() = _isJoinSucceeded

    private val _isUpdateSucceeded = MutableLiveData(false)
    val isUpdateSucceeded: LiveData<Boolean>
        get() = _isUpdateSucceeded

    private val _user : MutableStateFlow<User> =
        MutableStateFlow(User())
    val user get() = _user.asStateFlow()

    private val _loadingFlag = MutableLiveData(false)
    val loadingFlag : LiveData<Boolean>
        get() = _loadingFlag


    // 유저 회원가입
    suspend fun joinUser(user: UserDto) {
        joinUserUseCase.invoke(user).collect {
            if(it is ResultState.Success) {
                _user.value = it.data
                _isJoinSucceeded.value = true
                Log.d(TAG, "addUser ${TAG}: invoke Success!! $it")
                _loadingFlag.postValue(false)
            }
            else if (it is ResultState.Error) {
                Log.d(TAG, "addUser ${TAG}: invoke Fail!! $it")
                _loadingFlag.postValue(false)
            }
            else if(it is ResultState.Loading){
                    Log.d(TAG, "addUser ${TAG}: invoke Loading~")
                _loadingFlag.postValue(true)
            }
        }
    }

    // 유저 프로필 수정
    suspend fun updateUser(user: UserDto, prevProfileImageUrl: String?) {
        updateUserUseCase.invoke(user, prevProfileImageUrl).collect {
            if(it is ResultState.Success) {
                _user.value = it.data
                _isUpdateSucceeded.value = true
                Log.d(TAG, "updateUser ${TAG}: invoke Success!! $it")
                _loadingFlag.postValue(false)
            }
            else if (it is ResultState.Error) {
                Log.d(TAG, "updateUser ${TAG}: invoke Fail!! $it")
                _loadingFlag.postValue(false)
            }
            else if(it is ResultState.Loading){
                Log.d(TAG, "updateUser ${TAG}: invoke Loading~")
                _loadingFlag.postValue(true)
            }
        }
    }


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

    private fun isCorrectEmailPattern(email: String): Boolean {
        if (email.isEmpty())
            return false

        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

















    private val _accessToken = MutableLiveData<String>()
    val accessToken : LiveData<String>
        get() = _accessToken

    fun setToken(token: String){
        _accessToken.value = token
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