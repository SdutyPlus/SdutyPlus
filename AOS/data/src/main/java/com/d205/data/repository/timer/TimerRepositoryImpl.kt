package com.d205.data.repository.timer

import com.d205.data.repository.timer.local.TimerLocalDataSource
import com.d205.data.repository.timer.remote.TimerRemoteDataSource
import com.d205.domain.repository.TimerRepository
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val timerLocalDatasource: TimerLocalDataSource,
    private val timerRemoteDataSource: TimerRemoteDataSource
): TimerRepository {
    override suspend fun saveStartTimeOnTimer(startTime: String): Boolean {
        return timerLocalDatasource.saveStartTimeOnTimer(startTime)
    }

    override suspend fun getRealTime(): String {
        return timerRemoteDataSource.getRealTime()
    }


}