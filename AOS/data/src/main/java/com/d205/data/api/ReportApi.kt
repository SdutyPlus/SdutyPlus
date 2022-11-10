package com.d205.data.api

import com.d205.data.model.BaseResponse
import com.d205.data.model.report.ReportResponse
import com.d205.domain.model.report.Task
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReportApi {

    @GET("task/report/{date}")
    suspend fun getReport(@Path("date") date: String): BaseResponse<ReportResponse>

    @PUT("task/{task_seq}")
    suspend fun updateTask(@Path("task_seq") task_seq: Long, @Body task: Task): BaseResponse<Boolean>

}