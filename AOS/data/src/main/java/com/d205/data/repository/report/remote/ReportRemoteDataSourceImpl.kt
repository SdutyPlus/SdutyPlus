package com.d205.data.repository.report.remote

import com.d205.data.api.ReportApi
import com.d205.data.model.report.ReportResponse
import retrofit2.Response
import javax.inject.Inject

class ReportRemoteDataSourceImpl @Inject constructor(
    private val reportApi: ReportApi
): ReportRemoteDataSource {
    override suspend fun getReportList(date: String): Response<ReportResponse> {
        return reportApi.getReportListApi(date)
    }
}