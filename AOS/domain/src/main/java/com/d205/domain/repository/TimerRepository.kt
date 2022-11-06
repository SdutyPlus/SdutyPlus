package com.d205.domain.repository

interface TimerRepository {
    suspend fun saveStartTime(startTime: String): Boolean

    suspend fun getCurrentTime(): String
}