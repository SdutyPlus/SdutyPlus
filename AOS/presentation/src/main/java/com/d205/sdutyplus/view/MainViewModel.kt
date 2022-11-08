package com.d205.sdutyplus.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _bottomNavVisibility = MutableLiveData<Boolean>(false)
    val bottomNavVisibility : LiveData<Boolean>
        get() = _bottomNavVisibility

    init {
        _bottomNavVisibility.postValue(true)
    }

    fun displayBottomNav(show: Boolean){
        _bottomNavVisibility.postValue(show)
    }


    // User 정보 가져와서 저장
    private val _user = MutableLiveData<User>()
    val user : LiveData<User>
        get() = _user

    fun getUser(userSeq: Int) {
        viewModelScope.launch(Dispatchers.IO) {

        }
    }

    fun setUserValue(user: User){
        _user.postValue(user)
    }

}

