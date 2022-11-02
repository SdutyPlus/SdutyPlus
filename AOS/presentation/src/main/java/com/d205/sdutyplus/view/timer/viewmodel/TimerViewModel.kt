package com.d205.sdutyplus.view.timer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

private const val TAG = "TimerViewModel"
class TimerViewModel: ViewModel() {

    private val _isRunningTimer = MutableLiveData<Boolean>(false)
    val isRunningTimer: LiveData<Boolean>
        get() = _isRunningTimer

}