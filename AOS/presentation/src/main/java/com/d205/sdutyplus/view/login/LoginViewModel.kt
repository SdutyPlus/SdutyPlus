package com.d205.sdutyplus.view.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.User
import com.d205.domain.usecase.user.JoinNaverUserUseCase
import com.d205.domain.usecase.user.KakaoLoginUseCase
import com.d205.domain.usecase.user.NaverLoginUseCase
import com.d205.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG ="LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val kakaoLoginUseCase: KakaoLoginUseCase,
    private val naverLoginUseCase: NaverLoginUseCase,
    private val joinNaverUserUseCase: JoinNaverUserUseCase
): ViewModel() {
    private val _user : MutableStateFlow<ResultState<User>> = MutableStateFlow(ResultState.Uninitialized)
    val user get() = _user.asStateFlow()

//    val user : LiveData<User>
//        get() = _user

    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token
    fun setAccessToken(token: String){
        _token.postValue(token)
    }

    private val _isLoginSucceed = MutableLiveData<Boolean>(false)
    val isLoginSucceed: LiveData<Boolean>
        get() = _isLoginSucceed


    fun kakaoLogin(token: String) {
//        val tmp = kakaoLoginUseCase.invoke(token)
        //_isLoginSucceed.value = isLoginSucceeded()

        viewModelScope.launch {
            Log.d(TAG, "naverLogin $TAG: ")
            kakaoLoginUseCase.invoke(token).collect {
                if(it is ResultState.Success) {
                    _user.value = it
                    Log.d(TAG, "kakaoLogin User : ${it.data}")
                }
                else {
                    Log.d(TAG, "kakaoLogin $TAG: invoke Done!! $it")
                }
            }
        }
    }

    fun naverLogin(token: String) {
        viewModelScope.launch {
            //_user.value = naverLoginUseCase.invoke(token)

            //_isLoginSucceed.value = isLoginSucceeded()
            Log.d(TAG, "naverLogin $TAG: ")
            naverLoginUseCase.invoke(token).collect {
                if(it is ResultState.Success) {
                    _user.value = it
                    Log.d(TAG, "naverLogin User : ${it.data}")
                }
                else {
                    Log.d(TAG, "naverLogin $TAG: invoke Done!! $it")
                }

            }
        }
    }



//    private fun isLoginSucceeded(): Boolean =
//        user.value!!.seq != -1
}