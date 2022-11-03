package com.d205.data.repository.report.remote

import com.d205.data.model.report.ReportResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface ReportRemoteDataSource {
    fun getReportList(date: String): Flow<Response<ReportResponse>>
}