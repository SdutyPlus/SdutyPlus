package com.d205.data.repository.timer

import android.util.Log
import com.d205.data.mapper.mapperCurrentTimeResponseToCurrentTime
import com.d205.data.repository.timer.local.TimerLocalDataSource
import com.d205.data.repository.timer.remote.TimerRemoteDataSource
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.domain.repository.TimerRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val TAG = "TimerRepositoryImpl"
class TimerRepositoryImpl @Inject constructor(
    private val timerLocalDatasource: TimerLocalDataSource,
    private val timerRemoteDataSource: TimerRemoteDataSource
): TimerRepository {
    override fun saveStartTime(startTime: String): Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        timerLocalDatasource.saveStartTime(startTime).collect() {
            if(it) {
                emit(ResultState.Success(it))
            }
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override suspend fun updateStudyElapsedTime(studyTime: Int) {
        if(timerLocalDatasource.updateElapsedTime(studyTime)) {
            Log.d("ElapsedTime","$studyTime ElapsedTime")
        }
    }


    // flow, base response, result state와의 비교용 으로 기존 방식으로 구현된 코드
    override suspend fun getCurrentTime(): String { // remote 통신 실패 시 local 시간 반환
        var result  = timerRemoteDataSource.getRealTime()
        if(result != "error") {
            return mapperCurrentTimeResponseToCurrentTime(result)
        } else {
            return mapperCurrentTimeResponseToCurrentTime(timerLocalDatasource.getLocalCurrentTime())
        }
    }

    override fun getStartTime(): String {
        return timerLocalDatasource.getStartTime()
    }

    override fun getElapsedTime(): Int {
        return timerLocalDatasource.getStudyElapsedTime()
    }


    override fun getTodayTotalStudyTime(): Flow<ResultState<String>> = flow{
        timerRemoteDataSource.getTodayTotalStudyTime().collect { totalStudyTime ->
            if(totalStudyTime != "error") {
                emit(ResultState.Success(totalStudyTime))
            } else {
                Log.d(TAG, "getTodayTotalStudyTime repo error")
                emit(ResultState.Error(Exception("timer repo 통신 error")))
            }
        }
    }.catch { e ->
        Log.d(TAG, "getTodayTotalStudyTime error : $e")
        emit(ResultState.Error(e))
    }


//    {
//        var result  = timerRemoteDataSource.getTodayTotalStudyTime()
//        if(result != "error") {
//            return result
//        } else {
//            return "00:00:00"
//        }
//    }

    override fun addTask(currentTaskDto: CurrentTaskDto2): Flow<ResultState<Boolean>> = flow {

        timerRemoteDataSource.addTask(currentTaskDto).collect { isSuccessAdd ->

            emit(ResultState.Success(isSuccessAdd))
        }

    }.catch { e ->

    }

}