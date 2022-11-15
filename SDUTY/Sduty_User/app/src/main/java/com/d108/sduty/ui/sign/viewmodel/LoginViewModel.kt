package com.d108.sduty.ui.sign.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d108.sduty.common.ApplicationClass
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.JobHashtag
import com.d108.sduty.model.dto.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG ="LoginViewModel"
class LoginViewModel: ViewModel() {
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

    fun login(id: String, pw: String){
        viewModelScope.launch(Dispatchers.IO){
            val user = User(id, pw)
            Retrofit.userApi.login(user).let {
                if(it.isSuccessful && it.body() != null){
                    _user.postValue(it.body())
                    _isLoginSucceed.postValue(true)
                }else{
                    _isLoginSucceed.postValue(false)
                    Log.d(TAG, "login: ${it}")
                }
            }
        }
    }

    private val _isExistKakaoAccount = MutableLiveData<Boolean>()
    val isExistKakaoAccount: LiveData<Boolean>
        get() = _isExistKakaoAccount

    fun kakaoLogin(token: String){
        Log.d(TAG, "kakaoLogin: $token")
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.userApi.kakaoLogin(token).let {
                if(it.code() == 401){ // UNAUTHORIZED
                    _isExistKakaoAccount.postValue(false)
                    Log.d(TAG, "kakaoLogin: ${it.code()}")

                }else if(it.isSuccessful && it.body() != null){
                    _user.postValue(it.body())
                    _isLoginSucceed.postValue(true)
                }
                else{
                    Log.d(TAG, "kakaoLogin: ${it}")
                }
            }
        }
    }

    private val _isExistNaverAccount = MutableLiveData<Boolean>()
    val isExistNaverAccount: LiveData<Boolean>
        get() = _isExistNaverAccount

    fun naverLogin(token: String){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.userApi.naverLogin(token).let {
                if(it.code() == 401){ // UNAUTHORIZED
                    _isExistNaverAccount.postValue(false)
                    Log.d(TAG, "naverLogin: ${it}")

                }else if(it.isSuccessful && it.body() != null){
                    _user.postValue(it.body())
                    _isLoginSucceed.postValue(true)
                }
                else{
                    Log.d(TAG, "naverLogin: ${it}")
                }
            }
        }
    }

    fun getUserInfo(userId: String){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.userApi.getUserInfo(userId).let { 
                if(it.isSuccessful && it.body() != null){
                    _user.postValue(it.body())
                }else{
                    Log.d(TAG, "getUserInfo: ${it.code()}")
                }
                    
            }
        }
    }


    fun getJobListValue(){
        viewModelScope.launch(Dispatchers.IO){
            Retrofit.tagApi.getJobList().let {
                if(it.isSuccessful && it.body() != null){
                    setTagMap(it.body() as MutableList<JobHashtag>)
                }else{
                    Log.d(TAG, "getJobListValue: ${it.code()}")
                }
            }
        }
    }

    private fun setTagMap(list: MutableList<JobHashtag>){
        val map = hashMapOf<Int, String>()
        for(item in list) {
            map[item.seq] = item.name
        }
        ApplicationClass.jobTagMap = map
    }
}