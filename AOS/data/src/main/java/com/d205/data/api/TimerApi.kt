package com.d205.data.api

import com.d205.data.model.timer.RealTimeResponse
import com.d205.domain.model.user.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface TimerApi {

    @GET("timer")
    suspend fun getRealTime(): Response<RealTimeResponse>
}