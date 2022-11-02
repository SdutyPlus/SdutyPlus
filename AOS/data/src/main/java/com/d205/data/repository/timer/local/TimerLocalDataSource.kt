package com.d205.data.repository.timer.local

interface TimerLocalDataSource {
    suspend fun saveStartTimeOnTimer(startTime: String): Boolean
}