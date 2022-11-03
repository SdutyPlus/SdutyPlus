package com.d205.domain.repository

import com.d205.domain.model.report.Report
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getReportList(date: String): Flow<ResultState<Report>>
}