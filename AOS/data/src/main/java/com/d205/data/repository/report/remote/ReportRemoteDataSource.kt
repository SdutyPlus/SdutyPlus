package com.d205.data.repository.report.remote

import com.d205.data.api.BaseResponse
import com.d205.data.model.report.ReportResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ReportRemoteDataSource {
    fun getReport(date: String): Flow<BaseResponse<ReportResponse>>
}