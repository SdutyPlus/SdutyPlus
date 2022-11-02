package com.d205.data.repository.timer.local

import com.d205.data.dao.TimerSharedPreference
import javax.inject.Inject

class TimerLocalDataSourceImpl @Inject constructor(
    private val timerSharedPreference: TimerSharedPreference
): TimerLocalDataSource {
    override suspend fun saveStartTimeOnTimer(startTime: String): Boolean {
        return timerSharedPreference.setStringFromPreference("StartTimeOnTimer", startTime)
    }
}