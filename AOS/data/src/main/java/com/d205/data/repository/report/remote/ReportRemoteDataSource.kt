package com.d205.data.repository.report.remote

import com.d205.data.model.BaseResponse
import com.d205.data.model.report.DateResponse
import com.d205.data.model.report.GraphResponse
import com.d205.data.model.report.ReportResponse
import com.d205.data.model.report.TaskResponse
import com.d205.domain.model.report.Task
import kotlinx.coroutines.flow.Flow

interface ReportRemoteDataSource {
    fun getReport(date: String): Flow<BaseResponse<ReportResponse>>
    fun updateTask(task_seq: Long, task: Task): Flow<Boolean>
    fun deleteTask(task_seq: Long): Flow<Boolean>
    fun getDate(): Flow<BaseResponse<DateResponse>>
    fun getGraph(): Flow<BaseResponse<GraphResponse>>
}