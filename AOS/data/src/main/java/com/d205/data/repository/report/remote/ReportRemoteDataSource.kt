package com.d205.data.repository.report.remote

import com.d205.data.model.BaseResponse
import com.d205.data.model.report.ReportResponse
import kotlinx.coroutines.flow.Flow

interface ReportRemoteDataSource {
    fun getReport(date: String): Flow<BaseResponse<ReportResponse>>
}