package com.d205.data.api

import com.d205.data.model.report.ReportResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReportApi {

    @GET("tesk/{date}")
    suspend fun getReportListApi(@Path("date") date: String): Response<ReportResponse>

}