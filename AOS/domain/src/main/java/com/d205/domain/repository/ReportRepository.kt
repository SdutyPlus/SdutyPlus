package com.d205.domain.repository

import com.d205.domain.model.report.Graph
import com.d205.domain.model.report.Report
import com.d205.domain.model.report.SubTask
import com.d205.domain.model.report.Task
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getReport(date: String): Flow<ResultState<Report>>
    fun getTaskList(date: String): Flow<ResultState<List<Task>>>
    fun updateTask(task_seq: Long, task: Task): Flow<ResultState<Boolean>>
    fun deleteTask(task_seq: Long): Flow<ResultState<Boolean>>
    fun getGraph(): Flow<ResultState<Graph>>

}