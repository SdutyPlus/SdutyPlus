package com.d205.data.repository.timer.local

import com.d205.data.dao.TimerSharedPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class TimerLocalDataSourceImpl @Inject constructor(
    private val timerSharedPreference: TimerSharedPreference
): TimerLocalDataSource {
    override fun saveStartTime(startTime: String): Flow<Boolean> = flow {
        emit(timerSharedPreference.setStringFromPreference("StartTimeOnTimer", startTime))
    }

    override suspend fun getLocalCurrentTime(): String {
        return convertTimeDateToString(getTodayDate(), "yyyy-MM-dd HH:mm:ss")
    }

    override suspend fun updateElapsedTime(studyTime: Int): Boolean {
        return timerSharedPreference.setIntFromPreference("StudyElapsedTime",studyTime)
    }

    override fun getStartTime(): String {
        return timerSharedPreference.getStringFromPreference("StartTimeOnTimer")
    }

    override fun getStudyElapsedTime(): Int {
        return timerSharedPreference.getIntFromPreference("StudyElapsedTime")
    }


}

fun convertTimeDateToString(date: Date, format: String = "yyyy년 M월 d일"): String {
    val simpleDateFormat = SimpleDateFormat(format, Locale("ko", "KR"))
    return simpleDateFormat.format(date)
}

fun getTodayDate(): Date {
    return Date(System.currentTimeMillis())
}

