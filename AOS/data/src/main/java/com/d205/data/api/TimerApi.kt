package com.d205.data.api

import com.d205.data.model.timer.TimerResponseDto
import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.model.user.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TimerApi {

    @GET("timer")
    suspend fun getRealTime(): Response<TimerResponseDto>

    @GET("totalTime")
    suspend fun getTodayTotalStudyTime(): Response<TimerResponseDto>

    @POST("Task")
    suspend fun addTask(@Body currentTaskDto: CurrentTaskDto): Response<TimerResponseDto>
}