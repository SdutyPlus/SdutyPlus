package com.d205.data.repository.timer.remote

interface TimerRemoteDataSource {
    suspend fun getRealTime(): String

    suspend fun getTodayTotalStudyTime(): String
}