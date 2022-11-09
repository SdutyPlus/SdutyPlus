package com.d205.data.repository.timer.remote

import com.d205.domain.model.timer.CurrentTaskDto

interface TimerRemoteDataSource {
    suspend fun getRealTime(): String

    suspend fun getTodayTotalStudyTime(): String

    suspend fun addTask(currentTaskDto: CurrentTaskDto)
}