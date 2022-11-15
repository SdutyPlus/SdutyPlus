package com.d108.sduty.model.api

import com.d108.sduty.model.dto.Report
import com.d108.sduty.model.dto.Task
import retrofit2.Response
import retrofit2.http.*

interface TimerApi {

    // 사용자가 선택한 날짜의 리포트 목록을 받아온다.
    @GET("/report/{user_seq}/{date}")
    suspend fun getReport(@Path("user_seq") user_seq : Int, @Path("date") date: String): Response<Report>

    // 태스크를 등록한다.
    @POST("/report/tasks")
    suspend fun insertTask(@Body report: Report): Response<Unit>

    // 태스크를 수정한다.
    @PUT("/report/tasks/{task_seq}")
    suspend fun updateTask(@Path("task_seq") task_seq : Int, @Body task: Task): Response<Unit>

    // 태스크 하나를 삭제한다.
    @DELETE("/report/tasks/{task_seq}")
    suspend fun deleteTask(@Path("task_seq") task_seq: Int) : Response<Unit>
}