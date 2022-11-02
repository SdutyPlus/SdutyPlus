package com.d205.sdutyplus.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.User
import com.d205.domain.usecase.user.KakaoLoginUseCase
import com.d205.domain.usecase.user.NaverLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG ="LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val naverLoginUseCase: NaverLoginUseCase
): ViewModel() {
    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token
    fun setAccessToken(token: String){
        _token.postValue(token)
    }

    private val _isLoginSucceed = MutableLiveData<Boolean>()
    val isLoginSucceed: LiveData<Boolean>
        get() = _isLoginSucceed


    suspend fun kakaoLogin(token: String) {
        viewModelScope.launch {
            _user.value = kakaoLoginUseCase.invoke(token)
            _isLoginSucceed.value = isLoginSucceeded()
        }
    }

    suspend fun naverLogin(token: String) {
        viewModelScope.launch {
            _user.value = naverLoginUseCase.invoke(token)
            _isLoginSucceed.value = isLoginSucceeded()
        }
    }

    private fun isLoginSucceeded(): Boolean =
        user.value!!.seq != -1
}