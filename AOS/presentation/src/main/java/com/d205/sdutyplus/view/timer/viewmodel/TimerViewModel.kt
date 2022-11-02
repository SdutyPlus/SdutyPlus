package com.d205.sdutyplus.view.timer.viewmodel

import android.util.Log
import androidx.core.view.WindowInsetsAnimationCompat.Callback.DispatchMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.usecase.timer.SaveStartTimeOnTimerUsecase
import com.d205.sdutyplus.uitls.convertTimeDateToString
import com.d205.sdutyplus.uitls.getTodayDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer


private const val TAG = "TimerViewModel"
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val saveStartTimeUsecase: SaveStartTimeOnTimerUsecase
): ViewModel() {
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    private val _isTimerRunning = MutableLiveData<Boolean>(false)
    val isTimerRunning: LiveData<Boolean>
        get() = _isTimerRunning

    private val _timerTime = MutableLiveData<Int>(0)
    val timerTime: LiveData<Int>
        get() = _timerTime

    private var timerObj: Timer? = null


    fun startTimer() {
        _isTimerRunning.value = true

        timerObj = timer(period = 1000) {
            _timerTime.postValue(_timerTime.value!! + 1)
        }
    }

    fun saveStartTime() {
        val startTime = convertTimeDateToString(getTodayDate(), "yyyy-MM-dd HH:mm:ss")

        viewModelScope.launch(defaultDispatcher) {
            val saveStartTimeResult = saveStartTimeUsecase(startTime)
            if(saveStartTimeResult) {
                Log.d(TAG, "saveStartTime 성공")
            } else {
                Log.d(TAG, "saveStartTime 실패")
            }
        }
    }


}