package com.d108.sduty.ui.main.timer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.d108.sduty.common.ApplicationClass
import com.d108.sduty.model.Retrofit
import com.d108.sduty.model.dto.Report
import com.d108.sduty.model.dto.Task
import com.d108.sduty.utils.convertTimeDateToString
import com.d108.sduty.utils.convertTimeStringToDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timer

private const val TAG = "TimerViewModel"

class TimerViewModel() : ViewModel() {
    // 토스트 메시지 출력용
    private val _isInsertedTask = MutableLiveData<Boolean>()
    val isInsertedTask: LiveData<Boolean>
        get() = _isInsertedTask

    private val _isDeletedTask = MutableLiveData<Boolean>()
    val isDeletedTask: LiveData<Boolean>
        get() = _isDeletedTask

    private val _isUpdatedTask = MutableLiveData<Boolean>()
    val isUpdatedTask: LiveData<Boolean>
        get() = _isUpdatedTask

    private val _isErredConnection = MutableLiveData<Boolean>()
    val isErredConnection: LiveData<Boolean>
        get() = _isErredConnection

    // 선택된 날짜의 리포트
    private val _report = MutableLiveData<Report>()
    val report: LiveData<Report>
        get() = _report

    // 측정 중인 시간
    private val _timer = MutableLiveData<Int>(0)
    val timer: LiveData<Int>
        get() = _timer
    private var timerTask: Timer? = null

    // 유예 시간
    private val _delayTime = MutableLiveData<Int>(0)
    val delayTime: LiveData<Int>
        get() = _delayTime

    // 현재 동작 중인지 검사
    private val _isRunningTimer = MutableLiveData<Boolean>(false)
    val isRunningTimer: LiveData<Boolean>
        get() = _isRunningTimer

    // 현재 유예 중인지 검사
    private var isDelayingTimer = false

    // 선택된 날짜
    private var selectedDate = "1970-01-01"

    // 시간 측정 시작 시간
    private var _startTime: String = "00:00:00"
    val startTime: String
        get() = _startTime

    // 시간 측정 시작 시간
    private var _endTime: String = "00:00:00"
    val endTime: String
        get() = _endTime

    // 사용자가 날짜 선택
    fun selectDate(userSeq: Int, strDate: String) {
        val beforeDate = convertTimeStringToDate(strDate, "yyyy년 M월 d일")
        val convertedDate = convertTimeDateToString(beforeDate, "yyyy-MM-dd")
        getReport(userSeq, convertedDate)
        selectedDate = convertedDate
    }

    // 앱이 종료 되었을 경우 측정 시간을 복구한다.
    fun restoreTime(userSeq: Int) {
        val savedStartTime = ApplicationClass.timerPref.getString("StartTime", "")!!

        // 측정하던 정보가 남아 있을 경우
        if (savedStartTime.isNotEmpty() && !isRunningTimer.value!!) {
            val convertedStartTime =
                convertTimeStringToDate(savedStartTime!!, "yyyy-MM-dd HH:mm:ss")
            // 공부한 시간
            val studyTime = System.currentTimeMillis() - convertedStartTime.time

            _startTime = convertTimeDateToString(convertedStartTime, "HH:mm:ss")

            _timer.value = (studyTime / 1000).toInt()

            startTimer(userSeq)
        }
    }

    // 시간 측정을 시작한다.
    fun startTimer(userSeq: Int) {
        _isRunningTimer.value = true
        updateIsStudying(userSeq, true)

        timerTask = timer(period = 1000) {
            _timer.postValue(timer.value!! + 1)
            if (isDelayingTimer) {
                _delayTime.postValue(delayTime.value!! + 1)
            }

        }
    }

    // 측정 시작 시간을 저장한다.
    fun saveTime() {
        _startTime = convertTimeDateToString(Date(System.currentTimeMillis()), "HH:mm:ss")

        // 시작 시간을 디바이스에 저장
        ApplicationClass.timerPref.edit().putString(
            "StartTime",
            convertTimeDateToString(Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss")
        ).apply()
    }

    // 측정 시간을 유예한다.
    fun delayTimer() {
        isDelayingTimer = true
    }

    // 시간 측정을 종료한다.
    fun stopTimer(userSeq: Int) {
        updateIsStudying(userSeq, false)

        _endTime = convertTimeDateToString(Date(System.currentTimeMillis()), "HH:mm:ss")

        ApplicationClass.timerPref.edit().putString("StartTime", "").apply()
        _isRunningTimer.postValue(false)
        timerTask?.cancel()
        _timer.postValue(0)
        resetDelayTimer()
    }

    // 측정을 계속 한다.
    fun resetDelayTimer() {
        isDelayingTimer = false
        _delayTime.value = 0
    }

    fun saveTask(report: Report) {
        // Task 저장
        insertTask(report)
    }

    fun removeTask(userSeq: Int, position: Int) {
        val seq = report.value!!.tasks[position].seq
        deleteTask(userSeq, seq)
    }

    fun modifyTask(userSeq: Int, task: Task){
        updateTask(userSeq, task)
    }

    fun getTodayReport(userSeq: Int){
        val today = convertTimeDateToString(Date(System.currentTimeMillis()), "yyyy-MM-dd")
        getReport(userSeq, today)
    }

    fun resetLiveData(varName: String){
        when(varName){
            "isInsertedTask" -> {
                _isInsertedTask.postValue(false)
            }
            "isUpdateTask" -> {
                _isUpdatedTask.postValue(false)
            }
            "isDeletedTask" -> {
                _isDeletedTask.postValue(false)
            }
            "isErredConnection" -> {
                _isErredConnection.postValue(false)
            }
        }
    }

    /* API */

    // 선택된 날짜의 리포트를 요청
    private fun getReport(userSeq: Int, selectedDate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            Retrofit.timerApi.getReport(userSeq, selectedDate).let {
                // 사용자 리포트
                if (it.isSuccessful && it.body() != null) {
                    val report = it.body() as Report
                    report.tasks = report.tasks.sortedBy { it.startTime }
                    _report.postValue(report)
                } else if (it.code() == 401) {
                    // 못 받았을 때
                    Log.d(TAG, "getReport: error ${it.errorBody()}")
                    _report.postValue(Report(0, 0, selectedDate, "00:00:00", listOf()))
                } else {
                    _isErredConnection.postValue(true)
                }
            }
        }
    }

    // 시간 측정 기록 저장
    private fun insertTask(report: Report) {
        CoroutineScope(Dispatchers.IO).launch {
            Retrofit.timerApi.insertTask(report).let {
                if (it.isSuccessful) {
                    _isInsertedTask.postValue(true)
                    getTodayReport(report.ownerSeq)
                } else {
                    Log.e(TAG, "saveTask: ${it.code()}")
                    _isErredConnection.postValue(true)
                }
            }
        }
    }

    // 삭제
    private fun deleteTask(userSeq: Int, seq: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            Retrofit.timerApi.deleteTask(seq).let {
                if (it.isSuccessful) {
                    _isDeletedTask.postValue(true)
                    getTodayReport(userSeq)
                } else {
                    Log.e(TAG, "saveTask: ${it.code()}")
                    _isErredConnection.postValue(true)
                }
            }
        }
    }

    // 수정
    private fun updateTask(userSeq: Int, task: Task){
        CoroutineScope(Dispatchers.IO).launch {
            Retrofit.timerApi.updateTask(task.seq, task).let {
                if (it.isSuccessful) {
                    _isUpdatedTask.postValue(true)
                    getTodayReport(userSeq)
                } else {
                    Log.e(TAG, "saveTask: ${it.code()}")
                    _isErredConnection.postValue(true)
                }
            }
        }
    }

    // 공부 진행 중 상태 변경
    private fun updateIsStudying(userSeq: Int, isStudying: Boolean){
        val flag = if(isStudying) 1 else 0
        CoroutineScope(Dispatchers.IO).launch {
            Retrofit.profileApi.updateIsStudying(userSeq, flag).let {
                if (it.isSuccessful){
                    Log.d(TAG, "updateIsStudying: $isStudying")
                } else {
                    Log.d(TAG, "updateIsStudying: ${it.code()}")
                    _isErredConnection.postValue(true)
                }
            }
        }
    }

}