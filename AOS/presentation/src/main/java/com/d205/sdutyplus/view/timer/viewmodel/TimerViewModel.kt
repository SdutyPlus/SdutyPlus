package com.d205.sdutyplus.view.timer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.report.SubTask
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.domain.usecase.timer.*
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.utils.convertTimeDateToString
import com.d205.sdutyplus.utils.convertTimeStringToDate
import com.d205.sdutyplus.utils.getTodayDate
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
    private val startTimerUsecase: StartTimerUsecase,
    private val saveStartTimeUsecase: SaveStartTimeUsecase,
    private val getCurrentTimeUsecase: GetCurrentTimeUsecase,
    private val udateStudyElapsedTimeUsecase: UpdateStudyElapsedTimeUsecase,
    private val getTodayTotalStudyTimeUsecase: GetTodayTotalStudyTimeUsecase,
    private val getCurrentStudyTimeInfoUsecase: GetCurrentStudyTimeInfoUsecase,
    private val addTaskUsecase: AddTaskUsecase
): ViewModel() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
    private var timerObj: Timer? = null
    private var isResumeCountDownStart = false

    private val _isTimerRunning = MutableLiveData<Boolean>(false)
    val isTimerRunning: LiveData<Boolean>
        get() = _isTimerRunning

    private val _timerTime = MutableLiveData<Int>(0)
    val timerTime: LiveData<Int>
        get() = _timerTime

    private val _resumeCountDown = MutableLiveData<Int>(20)
    val resumeCountDown: LiveData<Int>
        get() = _resumeCountDown

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String>
        get() = _currentTime

    private val _todayTotalStudyTime = MutableLiveData<String>("00:00:00")
    val todayTotalStudyTime: LiveData<String>
        get() = _todayTotalStudyTime

    private val _updatedTotalTime = MutableLiveData<String>("00:00:00")
    val updatedTotalTime: LiveData<String>
        get() = _updatedTotalTime

    private val _loadingFlag = MutableLiveData(false)
    val loadingFlag : LiveData<Boolean>
        get() = _loadingFlag

    private val _addTaskCallBack = MutableLiveData<Int>(0)
    val addTaskCallBack: LiveData<Int>
        get() = _addTaskCallBack

    private val _stopTaskSaveCallback = MutableLiveData<Boolean>(false)
    val stopTaskSaveCallback: LiveData<Boolean>
        get() = _stopTaskSaveCallback


    fun callBackReset() {
        _addTaskCallBack.value = 0
        _stopTaskSaveCallback.value = false
    }


    // flow, base response, result state와의 비교용 으로 기존 방식으로 구현된 코드
    fun getCurrentTime() {
        viewModelScope.launch(defaultDispatcher){
            var result = getCurrentTimeUsecase()
            if(result != "error") {
                result = convertTime(result) // todo refactor
                _currentTime.postValue(result)
            }
        }
    }

    fun getTodayTotalStudyTime() {
        viewModelScope.launch(defaultDispatcher) {

            getTodayTotalStudyTimeUsecase.getTodayTotalStudyTime().collect() {
                if(it is ResultState.Success) {
                    _todayTotalStudyTime.postValue(it.data!!)
                }
            }
        }
    }

    private fun convertTime(time: String): String {
        val dateTime = convertTimeStringToDate(time, "yyyy-MM-dd HH:mm:ss")
        return convertTimeDateToString(dateTime,"yyyy년 M월 d일")
    }

    fun startTimer() {
        viewModelScope.launch(defaultDispatcher) {
            startTimerUsecase()
        }
        setTimer()
        updateTimerRunningState()
    }

    private fun updateTimerRunningState() {
        _isTimerRunning.postValue(!_isTimerRunning.value!!)
    }

    private fun setTimer() {
        timerObj = timer(period = 1000) {
            updateTimerTime()
            saveTimerTime()
            updateTotalStudyTime()
            if(isResumeCountDownStart) {
                updateCountDown()
            }
        }
    }

    private fun updateTimerTime() {
        _timerTime.postValue(_timerTime.value!! + 1) // todo refactoring
    }
    private fun saveTimerTime() {
        viewModelScope.launch(defaultDispatcher) {
            udateStudyElapsedTimeUsecase(_timerTime.value!!)
        }
    }
    private fun updateTotalStudyTime() {
        // 총 시간에 +1 해서 다시 넣어 준다.
        viewModelScope.launch(Dispatchers.Main) {
            var totalTime = _todayTotalStudyTime.value!!
            // 00:00:00을 초로 변환

            var token = totalTime.split(':')

            // 초로 변환 후 + 1
            var seconds = token[0].toInt() * 3600 + token[1].toInt() * 60 + token[2].toInt()
            seconds = seconds + _timerTime.value!!

            // 다시 00:00:00으로 변환 후 입력
            val hour = seconds / 60 / 60
            val min = (seconds / 60) % 60
            val sec = seconds % 60
            totalTime = String.format("%02d:%02d:%02d", hour, min, sec)


            _updatedTotalTime.value = totalTime
        }
    }

    private fun updateCountDown() {
        if(isResumeCountDownEnd()) {
            resumeCountDownReset()
        }
        _resumeCountDown.postValue(_resumeCountDown.value!! - 1)
    }

    private fun isResumeCountDownEnd(): Boolean {
        return _resumeCountDown.value!! == 0
    }

    fun resumeCountDownReset() {
        _resumeCountDown.postValue(20)
        isResumeCountDownStart = false
    }

    fun saveStartTime() {
        val startTime = convertTimeDateToString(getTodayDate(), "yyyy-MM-dd HH:mm:ss")

        viewModelScope.launch(defaultDispatcher) {
            saveStartTimeUsecase(startTime).collect() {
                if(it is ResultState.Success) {
                    Log.d(TAG, "saveStartTime 성공")
                } else {
                    Log.d(TAG, "saveStartTime 실패")
                }
            }
        }
    }


    fun startResumeCountDown() {
        isResumeCountDownStart = true
    }



    fun stopTimer() {
        _isTimerRunning.value = false
        timerObj!!.cancel()
    }

    fun timerTimeReset() {
        _timerTime.value = 0
        _updatedTotalTime.postValue(_todayTotalStudyTime.value)
    }


    fun addTask1(title: String, contents: List<SubTask> = mutableListOf()) {
        viewModelScope.launch(defaultDispatcher){
            var currentTaskDto = getCurrentStudyTimeInfoUsecase(_timerTime.value!!)
            currentTaskDto.title = title
            currentTaskDto.contents = contents


        }
    }

    fun addTask(title: String, contents: List<String>) {
        viewModelScope.launch(defaultDispatcher){

            var realContents: MutableList<String> = mutableListOf()
            for(content in contents) {
                if (content != "") {
                    realContents.add(content)
                }
            }

            var timeInfo = getCurrentStudyTimeInfoUsecase(_timerTime.value!!)
            var newTask = CurrentTaskDto2(0,timeInfo.startTime, timeInfo.endTime, title, realContents)

            addTaskUsecase(newTask).collect {
                if(it is ResultState.Success) {
                    _addTaskCallBack.postValue(200)
                    _loadingFlag.postValue(false)
                } else if (it is ResultState.Loading) {
                    _loadingFlag.postValue(true)
                }
                else {
                    _addTaskCallBack.postValue(400)
                    _loadingFlag.postValue(false)
                }
            }

        }
    }

}