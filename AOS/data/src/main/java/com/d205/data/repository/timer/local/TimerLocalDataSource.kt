package com.d205.data.repository.timer.local

interface TimerLocalDataSource {
    suspend fun saveStartTime(startTime: String): Boolean

    suspend fun getLocalCurrentTime(): String

    suspend fun updateElapsedTime(studyTime: Int): Boolean

    fun getStartTime(): String

    fun getStudyElapsedTime(): Int

}