package com.d205.domain.repository

import com.d205.domain.model.report.Task
import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    suspend fun saveStartTime(startTime: String): Boolean

    suspend fun getCurrentTime(): String

    suspend fun updateStudyElapsedTime(studyTime: Int)

    suspend fun getTodayTotalStudyTime(): String

    fun getElapsedTime(): Int

    fun getStartTime(): String

    fun addTask(currentTask: CurrentTaskDto2): Flow<ResultState<Boolean>>


}