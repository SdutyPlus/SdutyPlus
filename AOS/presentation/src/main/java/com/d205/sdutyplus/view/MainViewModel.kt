package com.d205.sdutyplus.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.User
import com.d205.domain.usecase.user.GetUserUseCase
import com.d205.domain.usecase.user.JoinUserUseCase
import com.d205.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    private val _bottomNavVisibility = MutableLiveData<Boolean>(false)
    val bottomNavVisibility : LiveData<Boolean>
        get() = _bottomNavVisibility

    init {
        _bottomNavVisibility.postValue(true)
    }

    fun displayBottomNav(show: Boolean){
        _bottomNavVisibility.postValue(show)
    }


    private val _user = MutableLiveData<User?>()
    val user : LiveData<User?>
        get() = _user

    // User 정보 가져와서 저장
    suspend fun getUser() {
        getUserUseCase.invoke().collect {
            if(it is ResultState.Success) {
                Log.d(TAG, "getUser invoke Success: ${it.data}")
                _user.postValue(it.data)
            }
        }
    }
}

