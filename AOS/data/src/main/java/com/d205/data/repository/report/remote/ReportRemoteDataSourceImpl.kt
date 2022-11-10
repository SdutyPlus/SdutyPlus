package com.d205.data.repository.report.remote

import android.annotation.SuppressLint
import android.util.Log
import com.d205.data.model.BaseResponse
import com.d205.data.api.ReportApi
import com.d205.data.model.report.ReportResponse
import com.d205.data.model.report.TaskResponse
import com.d205.domain.model.report.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val TAG = "ReportRemoteDataSourceImpl"
class ReportRemoteDataSourceImpl @Inject constructor(
    private val reportApi: ReportApi
): ReportRemoteDataSource {
    @SuppressLint("LongLogTag")
    override fun getReport(date: String): Flow<BaseResponse<ReportResponse>> = flow {
        emit(reportApi.getReport(date))
    }

    override fun updateTask(task_seq: Long, task: Task): Flow<Boolean> = flow {
        val response = reportApi.updateTask(task_seq, task)
        if(response.status == 200) {
            emit(true)
        } else {
            emit(false)
        }

    }.catch { e ->
        emit(false)
    }


}