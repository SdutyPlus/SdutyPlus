package com.d205.sdutyplus.view.timer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.usecase.timer.SaveStartTimeOnTimerUsecase
import com.d205.sdutyplus.uitls.convertTimeDateToString
import com.d205.sdutyplus.uitls.getTodayDate
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer

private const val TAG = "TimerViewModel"
class TimerViewModel @Inject constructor(
    private val saveStartTimeOnTimerUsecase: SaveStartTimeOnTimerUsecase, // todo StartTime 으로
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
): ViewModel() {

    private val _isRunningTimer = MutableLiveData<Boolean>(false) // todo timer Running
    val isRunningTimer: LiveData<Boolean>
        get() = _isRunningTimer

    private val _timerTime = MutableLiveData<Int>(0)
    val timerTime: LiveData<Int>
        get() = _timerTime

    private var timerObj: Timer? = null


    fun startTimer() {
        _isRunningTimer.value = true

        timerObj = timer(period = 1000) {
            _timerTime.postValue(_timerTime.value!! + 1)
        }
    }

    fun saveStartTimeOnTimer() {
        val startTime = convertTimeDateToString(getTodayDate(), "yyyy-MM-dd HH:mm:ss")

        viewModelScope.launch(defaultDispatcher) {
            val saveStartTimeResult = saveStartTimeOnTimerUsecase(startTime)
            if(saveStartTimeResult) {
                //todo 성공 처리
            } else {
                //todo 실패 처리
            }
        }

//        // 시작 시간을 디바이스에 저장
//        ApplicationClass.timerPref.edit().putString(
//            "StartTime",
//            convertTimeDateToString(Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss")
//        ).apply()
    }


}