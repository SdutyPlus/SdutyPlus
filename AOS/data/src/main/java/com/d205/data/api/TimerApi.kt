package com.d205.data.api

import com.d205.data.model.BaseResponse
import com.d205.data.model.timer.TimerResponseDto
import com.d205.domain.model.timer.CurrentTaskDto
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.domain.model.user.UserDto
import retrofit2.Response
import retrofit2.http.*

interface TimerApi {

    @GET("timer")
    suspend fun getRealTime(): Response<TimerResponseDto>

    @GET("task/report/time")
    suspend fun getTodayTotalStudyTime(): Response<TimerResponseDto>

    @POST("task")
    suspend fun addTask(@Body currentTaskDto: CurrentTaskDto2): BaseResponse<Void>
}