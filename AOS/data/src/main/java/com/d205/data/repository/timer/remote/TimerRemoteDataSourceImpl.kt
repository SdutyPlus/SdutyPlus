package com.d205.data.repository.timer.remote

import android.os.Build.VERSION_CODES.P
import android.util.Log
import com.d205.data.api.TimerApi
import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.model.timer.CurrentTaskDto2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.concurrent.timer

const val TAG = "TRemoteDS"
class TimerRemoteDataSourceImpl @Inject constructor(
    private val timerApi: TimerApi
): TimerRemoteDataSource {

    override suspend fun getRealTime(): String {
        val response = timerApi.getRealTime()
        if(response.isSuccessful && response.body() != null) {
            Log.d("timerApi", "getRealTime success ${response.body()!!.data}")
            return response.body()!!.data
        } else if(response.body() != null){
            Log.d("timerApi", "timerApi ${response.body()!!.code}")
            return "error"
        } else {
            Log.d(TAG, "통신 error")
            return "error"
        }

    }

    override suspend fun getTodayTotalStudyTime(): String {


            val response = timerApi.getTodayTotalStudyTime("2022-11-04")
            if (response.isSuccessful && response.body() != null) {
                Log.d("timerApi", "getTodayTotalStudyTime success ${response.body()!!.data}")
                return response.body()!!.data
            } else if (response.body() != null) {
                Log.d("timerApi", "getTodayTotalStudyTime error ${response.body()!!.code}")
                return "error"
            } else {
                Log.d("timerApi", "getTodayTotalStudyTime 통신 error")
                return "error"
            }

    }

    override fun addTask(currentTaskDto: CurrentTaskDto2): Flow<Boolean> = flow {

        Log.d("timerApi", "addTask tmp $currentTaskDto")
//        val response = timerApi.addTask(currentTaskDto)
        val response = timerApi.addTask(currentTaskDto)
        if(response.status == 200) {
            Log.d("timerApi", "addTask 성공 ${response.status}")
            emit(true)
        } else {
            Log.d("timerApi", "addTask 실패 ${response.status}")
            emit(false)
        }

    }.catch { e ->
        Log.d("timerApi", "통신 실패 $e")
        emit(false)
    }


}