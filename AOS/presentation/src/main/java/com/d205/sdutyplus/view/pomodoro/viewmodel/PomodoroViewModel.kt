package com.d205.sdutyplus.view.pomodoro.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel

class PomodoroViewModel: ViewModel() {
    private var pomodoroTimer: CountDownTimer? = null

    private var isStartPomodoro: Boolean = false

    fun updateStartedPomodoro() {
        isStartPomodoro = !isStartPomodoro
    }

}