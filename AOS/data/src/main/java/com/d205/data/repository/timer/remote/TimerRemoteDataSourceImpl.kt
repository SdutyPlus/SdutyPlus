package com.d205.data.repository.timer.remote

import android.util.Log
import com.d205.data.api.TimerApi
import javax.inject.Inject

class TimerRemoteDataSourceImpl @Inject constructor(
    private val timerApi: TimerApi
): TimerRemoteDataSource {

    override suspend fun getRealTime(): String {
        val response = timerApi.getRealTime()
        if(response.isSuccessful && response.body() != null) {
            Log.d("timerApi", "success ${response.body()!!.data}")
            return "성공"
        } else {
            Log.d("timerApi", "${response.body()!!.code}")
            return "error"
        }

    }
}