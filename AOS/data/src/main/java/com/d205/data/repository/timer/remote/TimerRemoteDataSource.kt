package com.d205.data.repository.timer.remote

import com.d205.domain.model.timer.CurrentTaskDto
import kotlinx.coroutines.flow.Flow

interface TimerRemoteDataSource {
    suspend fun getRealTime(): String

    suspend fun getTodayTotalStudyTime(): String

    suspend fun addTask(currentTaskDto: CurrentTaskDto): Flow<Boolean>
}