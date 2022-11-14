package com.d205.data.repository.timer.remote

import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.model.timer.CurrentTaskDto2
import kotlinx.coroutines.flow.Flow

interface TimerRemoteDataSource {
    suspend fun getRealTime(): String

    fun getTodayTotalStudyTime(): Flow<String>

    fun addTask(currentTaskDto: CurrentTaskDto2): Flow<Boolean>
}