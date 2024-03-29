package com.d205.sdutyplus.view.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.User
import com.d205.domain.usecase.user.*
import com.d205.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val TAG ="LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val naverLoginUseCase: NaverLoginUseCase,
    private val testLoginUseCase: TestLoginUseCase,
    private val autoLoginUseCase: AutoLoginUseCase,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {
    private val _user : MutableStateFlow<User> =
        MutableStateFlow(User())
    val user get() = _user.asStateFlow()

    private val _loadingFlag = MutableLiveData(false)
    val loadingFlag : LiveData<Boolean>
        get() = _loadingFlag

    var isLoginSucceeded = false
    var isJwtAvailable = false

    suspend fun kakaoLogin(token: String) {
        kakaoLoginUseCase.invoke(token).collect {
            if(it is ResultState.Success) {
                _user.value = it.data
                isLoginSucceeded = true
                Log.d(TAG, "kakaoLogin User : ${it.data}")
            }
            else {
                Log.d(TAG, "kakaoLogin $TAG: invoke Done!! $it")
            }
        }
    }

    suspend fun naverLogin(token: String) {
        naverLoginUseCase.invoke(token).collect {
            if(it is ResultState.Success) {
                _user.value = it.data
                isLoginSucceeded = true
                Log.d(TAG, "naverLogin User : ${it.data}")
            }
            else {
                Log.d(TAG, "naverLogin $TAG: invoke Done!! $it")
            }
        }
    }

    suspend fun testLogin() {
        testLoginUseCase().collect {
            if(it is ResultState.Success) {
                _user.value = it.data
                isLoginSucceeded = true
                _loadingFlag.postValue(false)
            }
            else if (it is ResultState.Error) {
                _loadingFlag.postValue(false)
            }
            else if(it is ResultState.Loading){
                _loadingFlag.postValue(true)
            }
        }
    }
    
    suspend fun checkJwt() {
        autoLoginUseCase.invoke().collect {
            if(it is ResultState.Success) {
                isJwtAvailable = it.data
                Log.d(TAG, "checkJwt: ${it.data}")
            }
            else {
                Log.d(TAG, "checkJwt: Failed!")
            }
        }
    }

    fun isJoinedUser(): Boolean {
        if(user.value.nickname == null) return false
        return user.value.nickname != ""
    }

    suspend fun getUser() {
        getUserUseCase.invoke().collect {
            if(it is ResultState.Success) {
                Log.d(TAG, "getUser invoke Success: ${it.data}")
                _user.value = it.data
            }
            else {
                Log.d(TAG, "getUser invoke Failed!")
            }
        }
    }
}