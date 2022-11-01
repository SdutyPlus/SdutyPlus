package com.d205.domain.repository

interface TimerRepository {
    suspend fun saveStartTimeOnTimer(startTime: String): Boolean
}