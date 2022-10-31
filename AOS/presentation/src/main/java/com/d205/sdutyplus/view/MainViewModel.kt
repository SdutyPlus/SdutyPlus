package com.d205.sdutyplus.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

}

