package com.d205.data.api

import com.d205.data.model.report.ReportResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ReportApi {

    @GET("task/{date}")
    suspend fun getReportApi(@Path("date") date: String): BaseResponse<ReportResponse>

}