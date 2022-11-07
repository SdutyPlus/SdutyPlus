package com.d205.data.api

import com.d205.data.model.BaseResponse
import com.d205.data.model.report.ReportResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ReportApi {

    @GET("task/{date}")
    suspend fun getReport(@Path("date") date: String): BaseResponse<ReportResponse>

}