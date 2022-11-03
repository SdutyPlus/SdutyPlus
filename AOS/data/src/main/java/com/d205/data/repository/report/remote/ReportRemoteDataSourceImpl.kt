package com.d205.data.repository.report.remote

import android.util.Log
import com.d205.data.api.ReportApi
import com.d205.data.model.report.ReportResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

const val TAG = "ReportRemoteDataSourceImpl"
class ReportRemoteDataSourceImpl @Inject constructor(
    private val reportApi: ReportApi
): ReportRemoteDataSource {
    override fun getReportList(date: String): Flow<Response<ReportResponse>> = flow {
        Log.d("TAG", "getReportList: $date")
        emit(reportApi.getReportListApi(date))

    }
}