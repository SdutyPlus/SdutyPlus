package com.d205.data.repository.timer

import android.util.Log
import com.d205.data.repository.timer.local.TimerLocalDataSource
import com.d205.data.repository.timer.remote.TimerRemoteDataSource
import com.d205.domain.model.report.Task
import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.repository.TimerRepository
import javax.inject.Inject

class TimerRepositoryImpl @Inject constructor(
    private val timerLocalDatasource: TimerLocalDataSource,
    private val timerRemoteDataSource: TimerRemoteDataSource
): TimerRepository {
    override suspend fun saveStartTime(startTime: String): Boolean {
        return timerLocalDatasource.saveStartTime(startTime)
    }

    override suspend fun getCurrentTime(): String { // remote 통신 실패 시 local 시간 반환
        var result  = timerRemoteDataSource.getRealTime()
        if(result != "error") {
            return result
        } else {
            return timerLocalDatasource.getLocalCurrentTime()
        }
    }

    override suspend fun updateStudyElapsedTime(studyTime: Int) {
        if(timerLocalDatasource.updateElapsedTime(studyTime)) {
            Log.d("ElapsedTime","$studyTime ElapsedTime")
        }
    }

    override suspend fun getTodayTotalStudyTime(): String {
        return timerRemoteDataSource.getTodayTotalStudyTime()
    }

    override fun getCurrentStudyInfo(): CurrentTaskDto {
        var startTime = getStartTime()
        var studyElapsedTime = getStudyElapsedTime()

        var endTime = calculateEndTime()

        return CurrentTaskDto(0,startTime,endTime,studyElapsedTime, "title")

    }

    private fun getStartTime(): String {
        return timerLocalDatasource.getStartTime()
    }

    private fun getStudyElapsedTime(): Int {
        return timerLocalDatasource.getStudyElapsedTime()
    }

    private fun calculateEndTime() {

    }


}