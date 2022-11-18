package com.d205.domain.repository

import com.d205.domain.model.report.Task
import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    fun saveStartTime(startTime: String): Flow<ResultState<Boolean>>

    suspend fun getCurrentTime(): String

    suspend fun updateStudyElapsedTime(studyTime: Int)

    fun getTodayTotalStudyTime(): Flow<ResultState<String>>

    fun getElapsedTime(): Int

    fun getStartTime(): String

    fun addTask(currentTask: CurrentTaskDto2): Flow<ResultState<Boolean>>


}