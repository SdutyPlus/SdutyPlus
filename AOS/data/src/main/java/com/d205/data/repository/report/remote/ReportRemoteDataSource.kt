package com.d205.data.repository.report.remote

import com.d205.data.model.report.ReportResponse
import retrofit2.Response

interface ReportRemoteDataSource {
    suspend fun getReportList(date: String): Response<ReportResponse>
}