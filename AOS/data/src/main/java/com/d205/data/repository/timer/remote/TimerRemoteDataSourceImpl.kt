package com.d205.data.repository.timer.remote

import android.util.Log
import com.d205.data.api.TimerApi
import com.d205.domain.model.timer.CurrentTaskDto
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
        val response = timerApi.getTodayTotalStudyTime()
        if(response.isSuccessful && response.body() != null) {
            Log.d("timerApi", "getTodayTotalStudyTime success ${response.body()!!.data}")
            return response.body()!!.data
        } else if(response.body() != null){
            Log.d("timerApi", "getTodayTotalStudyTime error ${response.body()!!.code}")
            return "error"
        } else {
            Log.d(TAG, "통신 error")
            return "error"
        }
    }

    val flag = 1
    override suspend fun addTask(currentTaskDto: CurrentTaskDto) {
        //if(flag = 1)
        val response = timerApi.addTask(currentTaskDto)
        if(response.isSuccessful && response.body() != null) {
            Log.d("timerApi", "addTask success ${response.body()!!.data}")
        } else if(response.body() != null){
            Log.d("timerApi", "addTask error ${response.body()!!.code}")
        } else {
            Log.d(TAG, "통신 error")
        }
    }


}