package com.d205.data.repository.report.remote

import android.annotation.SuppressLint
import android.util.Log
import com.d205.data.model.BaseResponse
import com.d205.data.api.ReportApi
import com.d205.data.model.report.ReportResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val TAG = "ReportRemoteDataSourceImpl"
class ReportRemoteDataSourceImpl @Inject constructor(
    private val reportApi: ReportApi
): ReportRemoteDataSource {
    @SuppressLint("LongLogTag")
    override fun getReport(date: String): Flow<BaseResponse<ReportResponse>> = flow {
        Log.d(TAG, "getReportListasd: ${reportApi.getReport(date)}")
        emit(reportApi.getReport(date))
    }
}