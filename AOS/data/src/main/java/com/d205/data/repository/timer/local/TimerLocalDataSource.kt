package com.d205.data.repository.timer.local

import kotlinx.coroutines.flow.Flow

interface TimerLocalDataSource {
    fun saveStartTime(startTime: String): Flow<Boolean>

    suspend fun getLocalCurrentTime(): String

    suspend fun updateElapsedTime(studyTime: Int): Boolean

    fun getStartTime(): String

    fun getStudyElapsedTime(): Int

}