package com.d205.domain.repository

import com.d205.domain.model.report.Report
import com.d205.domain.model.report.Task
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getReport(date: String): Flow<ResultState<String>>
    fun getTaskList(date: String): Flow<ResultState<List<Task>>>
}