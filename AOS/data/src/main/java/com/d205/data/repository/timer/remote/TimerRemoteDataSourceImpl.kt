package com.d205.data.repository.timer.remote

import android.util.Log
import com.d205.data.api.TimerApi
import javax.inject.Inject
import kotlin.concurrent.timer

const val TAG = "TRemoteDS"
class TimerRemoteDataSourceImpl @Inject constructor(
    private val timerApi: TimerApi
): TimerRemoteDataSource {

    override suspend fun getRealTime(): String {
        val response = timerApi.getRealTime()
        if(response.isSuccessful && response.body() != null) {
            Log.d("timerApi", "timerApi success ${response.body()!!.data}")
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
            return response.body()!!.data
        } else {
            return "30:30:30"
        }
    }
}